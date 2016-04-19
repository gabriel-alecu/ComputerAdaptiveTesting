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
import java.util.concurrent.TimeUnit;

@Service
public class QuizService {
    public static final int MAX_TIME = 30;  //minutes

    public static final int MAX_QUESTIONS = 30;
    public static final int MAX_SAME_SCORE = 5;
    public static final int MAX_CONSECUTIVE_WRONG = 5;

    public static final int MAX_QUESTION_SCORE = 5;
    public static final int MIN_QUESTION_SCORE = 1;


    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;


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

    public Question chooseNextQuestion(OngoingQuiz ongoingQuiz) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        if(ongoingQuiz.getEndTime().before(now)) {
            return null;
        }

        switch(ongoingQuiz.getQuestionsResponses().size()) {
            case MAX_QUESTIONS:
                return null;
            case 0:
                return getQuestionWithScore(5, ongoingQuiz);
            default:
                List<QuestionResponse> responses = ongoingQuiz.getQuestionsResponses();

                Integer numSameScore = 0;
                Integer numWrong = 0;
                Integer lastCorrectScore = null;
                Boolean prevWasWrong = true;
                for(int i=responses.size()-1; i>=0; i--) {
                    QuestionResponse response = responses.get(i);
                    if(isCorect(response)) {
                        prevWasWrong = false;
                        if(lastCorrectScore == null) {
                            lastCorrectScore = response.getQuestion().getScore();
                        } else {
                            if(response.getQuestion().getScore() == lastCorrectScore) {
                                numSameScore++;
                                if(numSameScore >= MAX_SAME_SCORE) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else if(prevWasWrong) {
                        numWrong++;
                        if(numWrong >= MAX_CONSECUTIVE_WRONG) {
                            break;
                        }
                    }
                }
                if(numSameScore >= MAX_SAME_SCORE || numWrong >= MAX_CONSECUTIVE_WRONG) {
                    return null;
                }


                QuestionResponse lastResponse = responses.get(responses.size()-1);
                Integer nextQuestionScore;
                if (isCorect(lastResponse)) {
                    nextQuestionScore = Math.min(lastResponse.getQuestion().getScore() + 1, MAX_QUESTION_SCORE);
                } else {
                    nextQuestionScore = Math.max(lastResponse.getQuestion().getScore() - 1, MIN_QUESTION_SCORE);
                }
                return getQuestionWithScore(nextQuestionScore, ongoingQuiz);
        }
    }

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

    public Boolean isCorect(QuestionResponse questionResponse) {
        Set<Answer> correctAnswers = getCorrectAnswers(questionResponse.getQuestion());
        Set<Answer> userAnswers = questionResponse.getSelectedAnswers();
        return correctAnswers.equals(userAnswers);
    }

    public Integer calcFinalScore(OngoingQuiz ongoingQuiz) {
        List<QuestionResponse> responses = ongoingQuiz.getQuestionsResponses();
        Integer totalScore = 0;
        Integer totalPossibleScore = 0;
        for(QuestionResponse response : responses) {
            totalPossibleScore += response.getQuestion().getScore();
            if(isCorect(response)) {
                totalScore += response.getQuestion().getScore();
            }
        }

        Integer finalScore;
        if(totalPossibleScore.equals(0)) {
            finalScore = 0;
        } else {
            finalScore = totalScore*100/totalPossibleScore;
        }
        return finalScore;
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
        System.out.println(isCorect(lastResponse));
    }
}
