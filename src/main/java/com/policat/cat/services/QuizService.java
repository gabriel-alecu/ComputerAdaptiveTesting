package com.policat.cat.services;

import com.policat.cat.entities.Answer;
import com.policat.cat.entities.Question;
import com.policat.cat.entities.Quiz;
import com.policat.cat.repositories.QuestionRepository;
import com.policat.cat.repositories.QuizRepository;
import com.policat.cat.temp_containers.OngoingQuiz;
import com.policat.cat.temp_containers.QuestionResponse;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {
    public static final int MAX_QUESTION_SCORE = 5;
    public static final int MIN_QUESTION_SCORE = 1;

    public static final int MIN_QUESTIONS = 5;
    public static final int MAX_QUESTIONS = 25;
    public static final int MAX_QUESTION_TIME = 60;  //seconds


    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;


    public Quiz getQuizWithQuestions(Long quizId) {
        Quiz quiz = quizRepository.findOne(quizId);
        Hibernate.initialize(quiz.getQuestions());
        return quiz;
    }

    public Question getQuestionAnswers(Question question) {
        question = questionRepository.findOne(question.getId());
        Hibernate.initialize(question.getAnswers());
        return question;
    }

    public Set<Answer> getCorrectAnswers(Question question) {
        Set<Answer> correctAnswers = new HashSet<>();
        for(Answer answer : question.getAnswers()) {
            if(answer.getCorrect()) {
                correctAnswers.add(answer);
            }
        }
        return correctAnswers;
    }

    public Boolean isCorrect(QuestionResponse questionResponse) {
        Set<Answer> correctAnswers = getCorrectAnswers(questionResponse.getQuestion());
        Set<Answer> userAnswers = questionResponse.getSelectedAnswers();
        return correctAnswers.equals(userAnswers);
    }


    public QuestionResponse getPreviousResponse(OngoingQuiz ongoingQuiz) {
        List<QuestionResponse> responses = ongoingQuiz.getQuestionsResponses();
        return responses.get(responses.size()-1);
    }

    public Question getQuestionWithScore(Integer score, OngoingQuiz ongoingQuiz) {
        List<Long> usedQuestions = new ArrayList<>();
        for(QuestionResponse questionResponse : ongoingQuiz.getQuestionsResponses()) {
            Question question = questionResponse.getQuestion();
            usedQuestions.add(question.getId());
        }
        List<Question> available;
        if(usedQuestions.isEmpty()) {
            available = questionRepository.findByScore(ongoingQuiz.getQuiz(), score);
        } else {
            available = questionRepository.findNewByScore(ongoingQuiz.getQuiz(), score, usedQuestions);
        }
        if(available.isEmpty()) {
            return null;
        }

        Random randGen = new Random();
        Integer chosenPos = randGen.nextInt(available.size());
        Question chosen = available.get(chosenPos);
        return getQuestionAnswers(chosen);
    }

    public List<QuestionResponse> getCorrectResponses(OngoingQuiz ongoingQuiz) {
        List<QuestionResponse> correctResponses = new ArrayList<>();
        List<QuestionResponse> responses = ongoingQuiz.getQuestionsResponses();
        for(QuestionResponse response : responses) {
            if(isCorrect(response)) {
                correctResponses.add(response);
            }
        }
        return correctResponses;
    }

    public Double calcMean(OngoingQuiz ongoingQuiz) {
        Integer sumScores = 0;
        List<QuestionResponse> correctResponses = getCorrectResponses(ongoingQuiz);
        for(QuestionResponse response : correctResponses) {
            sumScores += response.getQuestion().getScore();
        }
        return sumScores/(double)correctResponses.size();
    }

    public Double calcError(OngoingQuiz ongoingQuiz) {
        //Standard deviation
        Double mean = calcMean(ongoingQuiz);
        Double devSum = 0.0;
        List<QuestionResponse> correctResponses = getCorrectResponses(ongoingQuiz);
        for(QuestionResponse response : correctResponses) {
            Integer questionScore = response.getQuestion().getScore();
            devSum += Math.pow((questionScore - mean), 2);
        }
        Double stdDev = Math.sqrt(devSum/correctResponses.size());

        //Standard error
        return stdDev/Math.sqrt(correctResponses.size());
    }

    public Question chooseNextQuestion(OngoingQuiz ongoingQuiz) {
        if (ongoingQuiz.getQuestionsResponses().isEmpty()) {
            return getQuestionWithScore(5, ongoingQuiz);
        } else if (ongoingQuiz.getQuestionsResponses().size() >= MAX_QUESTIONS) {
            return null;
        }

        if (ongoingQuiz.getQuestionsResponses().size() > MIN_QUESTIONS && calcError(ongoingQuiz) < .4) {
            return null;
        }

        QuestionResponse lastResponse = getPreviousResponse(ongoingQuiz);
        Integer nextQuestionScore;
        if (isCorrect(lastResponse)) {
            nextQuestionScore = Math.min(lastResponse.getQuestion().getScore() + 1, MAX_QUESTION_SCORE);
        } else {
            nextQuestionScore = Math.max(lastResponse.getQuestion().getScore() - 1, MIN_QUESTION_SCORE);
        }
        return getQuestionWithScore(nextQuestionScore, ongoingQuiz);
    }

    public Integer calcFinalScore(OngoingQuiz ongoingQuiz) {
        Double mean = calcMean(ongoingQuiz);
        return (int)(100*mean/MAX_QUESTION_SCORE);
    }

    public void debugLastResponse(OngoingQuiz ongoingQuiz) {
        QuestionResponse lastResponse = ongoingQuiz.getQuestionsResponses().get(ongoingQuiz.getQuestionsResponses().size() - 1);

        System.out.print("Question ID: ");
        System.out.println(lastResponse.getQuestion().getId());

        System.out.print("Score: ");
        System.out.println(lastResponse.getQuestion().getScore());

        System.out.print("Corect Answers IDs:");
        for(Answer answer : getCorrectAnswers(lastResponse.getQuestion())) {
            System.out.print(" ");
            System.out.print(answer.getId());
        }
        System.out.println();

        System.out.print("Selected Answers IDs:");
        for(Answer answer : lastResponse.getSelectedAnswers()) {
            System.out.print(" ");
            System.out.print(answer.getId());
        }
        System.out.println();

        System.out.print("Is Correct: ");
        System.out.println(isCorrect(lastResponse));
    }
}
