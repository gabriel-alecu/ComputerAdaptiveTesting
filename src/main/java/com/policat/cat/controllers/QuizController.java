package com.policat.cat.controllers;

import com.policat.cat.auth.AuthedUser;
import com.policat.cat.entities.*;
import com.policat.cat.repositories.QuizResultRepository;
import com.policat.cat.repositories.QuizRepository;
import com.policat.cat.services.QuizService;
import com.policat.cat.temp_containers.OngoingQuiz;
import com.policat.cat.temp_containers.QuestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/quiz")
@SessionAttributes("ongoingQuiz")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizResultRepository quizResultRepository;


    @ModelAttribute("ongoingQuiz")
    public OngoingQuiz getDefaultQuiz() {
        return new OngoingQuiz();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showQuizzesList(Model model) {
        AuthedUser auth = (AuthedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = auth.getUser();

        model.addAttribute("quizzes", quizRepository.findAll());
        model.addAttribute("resolved", quizResultRepository.findByUser(user));
        return "quiz_index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String startQuiz(@ModelAttribute("quiz") Long quizId, Model model) {
        Quiz quiz = quizService.getQuizWithQuestions(quizId);
        if (quiz == null) {
            return "quiz";
        }

        OngoingQuiz ongoingQuiz = new OngoingQuiz();
        ongoingQuiz.setQuiz(quiz);
        model.addAttribute("ongoingQuiz",ongoingQuiz);

        return "redirect:/quiz/question";
    }

    @RequestMapping(value="/question", method = RequestMethod.GET)
    public String displayChoice(@ModelAttribute("ongoingQuiz") OngoingQuiz ongoingQuiz, SessionStatus sessionStatus) {
        //if you get on this page directly, without starting a quiz first
        if(ongoingQuiz.getQuiz() == null || ongoingQuiz.getCompleted()) {
            sessionStatus.setComplete();
            return "redirect:/quiz";
        }

        //replace it only if it's not already set (to prevent using page refresh in order to get a new one)
        Question currentQuestion = ongoingQuiz.getCurrentQuestion();
        if(currentQuestion == null) {
            currentQuestion = quizService.chooseNextQuestion(ongoingQuiz);
        }
        ongoingQuiz.setCurrentQuestion(currentQuestion);

        //if there are no more questions available, end the quiz
        if(currentQuestion == null) {
            ongoingQuiz.setCompleted(true);
            return "redirect:/quiz/result";
        }

        return "quiz_question";
    }

    @RequestMapping(value="/question", method = RequestMethod.POST)
    public String processChoice(@ModelAttribute("ongoingQuiz") OngoingQuiz ongoingQuiz, SessionStatus sessionStatus) {
        if(ongoingQuiz.getQuiz() == null || ongoingQuiz.getCompleted()) {
            sessionStatus.setComplete();
            return "redirect:/quiz";
        }

        Question newQuestion = quizService.chooseNextQuestion(ongoingQuiz);
        if(newQuestion == null) {
            ongoingQuiz.setCompleted(true);
            return "redirect:/quiz/result";
        }


        Set<Answer> selectedAnswers = new HashSet<>();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        //If time limit was reached, we don't take in account the answers
        if(ongoingQuiz.getQuestionTimeLimit().after(now)) {
            for (Long chosenId : ongoingQuiz.getCurrentSelectedAnswers()) {
                for (Answer answer : ongoingQuiz.getCurrentQuestion().getAnswers()) {
                    if (answer.getId().equals(chosenId)) {
                        selectedAnswers.add(answer);
                        break;
                    }
                }
            }
        }

        QuestionResponse questionResponse = new QuestionResponse(ongoingQuiz.getCurrentQuestion(), selectedAnswers);
        ongoingQuiz.addQuestionResponse(questionResponse);

        quizService.debugLastResponse(ongoingQuiz);


        //prepare to choose a new question
        ongoingQuiz.setCurrentQuestion(null);

        return this.displayChoice(ongoingQuiz, sessionStatus);
    }

    @RequestMapping(value="/result", method = RequestMethod.GET)
    public String displayResults(@ModelAttribute("ongoingQuiz") OngoingQuiz ongoingQuiz, SessionStatus sessionStatus, Model model) {
        if(ongoingQuiz.getQuiz() == null) {
            sessionStatus.setComplete();
            return "redirect:/quiz";
        }

        if(!ongoingQuiz.getCompleted()) {
            return "redirect:/quiz/question";
        }

        Integer score = quizService.calcFinalScore(ongoingQuiz);

        AuthedUser auth = (AuthedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = auth.getUser();

        Quiz quiz = ongoingQuiz.getQuiz();

        QuizResult quizResult = new QuizResult(score, user, quiz);
        quizResultRepository.save(quizResult);

        model.addAttribute(quizResult);

        return "quiz_result";
    }
}
