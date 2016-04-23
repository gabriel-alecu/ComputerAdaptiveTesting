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

    public static final int START_QUESTION_SCORE = 3;
    public static final int NUM_SETUP_QUESTIONS = 2;

    public static final int MIN_QUESTIONS = 5;
    public static final int MAX_QUESTIONS = 25;
    public static final int MAX_QUESTION_TIME = 60;  //seconds
    public static final double ERROR_LIMIT = .2;


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

    public Integer computedResponseScore(QuestionResponse questionResponse) {
        Integer questionScore = questionResponse.getQuestion().getScore();
        if(questionResponse.isCorrect()) {
            questionScore = Math.min(++questionScore, MAX_QUESTION_SCORE);
        } else {
            --questionScore;
        }
        return questionScore;
    }

    public List<QuestionResponse> getScoreResponses(OngoingQuiz ongoingQuiz) {
        List<QuestionResponse> responses = ongoingQuiz.getQuestionsResponses();
        if(responses.size() <= NUM_SETUP_QUESTIONS) {
            return responses.subList(responses.size(), responses.size());
        }
        return responses.subList(NUM_SETUP_QUESTIONS, responses.size());
    }

    public Double calcMean(OngoingQuiz ongoingQuiz) {
        Integer sumScores = 0;
        List<QuestionResponse> responses = getScoreResponses(ongoingQuiz);

        if(responses.size() == 0) {
            return 0.0;
        }

        for(QuestionResponse response : responses) {
            sumScores += computedResponseScore(response);
        }

        return sumScores/(double)responses.size();
    }

    public Double calcError(OngoingQuiz ongoingQuiz) {
        //Standard deviation
        Double mean = calcMean(ongoingQuiz);
        Double devSum = 0.0;
        List<QuestionResponse> responses = getScoreResponses(ongoingQuiz);

        if(responses.size() == 0) {
            return 0.0;
        }

        for(QuestionResponse response : responses) {
            Integer questionScore = computedResponseScore(response);
            devSum += Math.pow((questionScore - mean), 2);
        }
        Double stdDev = Math.sqrt(devSum/responses.size());

        //Standard error
        return stdDev/Math.sqrt(responses.size());
    }

    public Question chooseNextQuestion(OngoingQuiz ongoingQuiz) {
        if (ongoingQuiz.getQuestionsResponses().isEmpty()) {
            return getQuestionWithScore(START_QUESTION_SCORE, ongoingQuiz);
        } else if (ongoingQuiz.getQuestionsResponses().size() >= MAX_QUESTIONS) {
            return null;
        }

        if (ongoingQuiz.getQuestionsResponses().size() > MIN_QUESTIONS && calcError(ongoingQuiz) < ERROR_LIMIT) {
            return null;
        }

        QuestionResponse lastResponse = getPreviousResponse(ongoingQuiz);
        Integer nextQuestionScore;
        if (lastResponse.isCorrect()) {
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
        List<QuestionResponse> responses = ongoingQuiz.getQuestionsResponses();
        QuestionResponse lastResponse = responses.get(ongoingQuiz.getQuestionsResponses().size() - 1);

        System.out.print("Question ID: ");
        System.out.println(lastResponse.getQuestion().getId());

        System.out.print("Score: ");
        System.out.println(lastResponse.getQuestion().getScore());

        System.out.print("Corect Answers IDs:");
        for(Answer answer : lastResponse.getQuestion().getCorrectAnswers()) {
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
        System.out.println(lastResponse.isCorrect());

        System.out.print("Current Score: ");
        System.out.println(calcMean(ongoingQuiz));

        System.out.print("Current Error: ");
        System.out.println(calcError(ongoingQuiz));
    }
}
