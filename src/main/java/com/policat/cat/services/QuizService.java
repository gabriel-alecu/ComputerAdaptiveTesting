package com.policat.cat.services;

import com.policat.cat.entities.Option;
import com.policat.cat.entities.Domain;
import com.policat.cat.entities.Question;
import com.policat.cat.repositories.QuestionRepository;
import com.policat.cat.repositories.DomainRepository;
import com.policat.cat.temp_containers.Quiz;
import com.policat.cat.temp_containers.Response;
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
    DomainRepository domainRepository;

    @Autowired
    QuestionRepository questionRepository;


    public Domain getDomainWithQuestions(Long id) {
        Domain domain = domainRepository.findOne(id);
        Hibernate.initialize(domain.getQuestions());
        return domain;
    }

    public Question getQuestionOptions(Question question) {
        question = questionRepository.findOne(question.getId());
        Hibernate.initialize(question.getOptions());
        return question;
    }

    public Response getPreviousResponse(Quiz quiz) {
        List<Response> responses = quiz.getResponses();
        return responses.get(responses.size()-1);
    }

    public Question getQuestionWithScore(Integer score, Quiz quiz) {
        List<Long> usedQuestions = new ArrayList<>();
        for(Response response : quiz.getResponses()) {
            Question question = response.getQuestion();
            usedQuestions.add(question.getId());
        }
        List<Question> available;
        if(usedQuestions.isEmpty()) {
            available = questionRepository.findByScore(quiz.getDomain(), score);
        } else {
            available = questionRepository.findNewByScore(quiz.getDomain(), score, usedQuestions);
        }
        if(available.isEmpty()) {
            return null;
        }

        Random randGen = new Random();
        Integer chosenPos = randGen.nextInt(available.size());
        Question chosen = available.get(chosenPos);
        return getQuestionOptions(chosen);
    }

    public Integer computedResponseScore(Response response) {
        Integer questionScore = response.getQuestion().getScore();
        if(response.isCorrect()) {
            questionScore = Math.min(++questionScore, MAX_QUESTION_SCORE);
        } else {
            --questionScore;
        }
        return questionScore;
    }

    public List<Response> getScoreResponses(Quiz quiz) {
        List<Response> responses = quiz.getResponses();
        if(responses.size() < NUM_SETUP_QUESTIONS) {
            return responses.subList(responses.size(), responses.size());
        }
        return responses.subList(NUM_SETUP_QUESTIONS, responses.size());
    }

    public Double calcMean(Quiz quiz) {
        Integer sumScores = 0;
        List<Response> responses = getScoreResponses(quiz);

        if(responses.size() == 0) {
            return 0.0;
        }

        for(Response response : responses) {
            sumScores += computedResponseScore(response);
        }

        return sumScores/(double)responses.size();
    }

    public Double calcError(Quiz quiz) {
        //Standard deviation
        Double mean = calcMean(quiz);
        Double devSum = 0.0;
        List<Response> responses = getScoreResponses(quiz);

        if(responses.size() == 0) {
            return 0.0;
        }

        for(Response response : responses) {
            Integer questionScore = computedResponseScore(response);
            devSum += Math.pow((questionScore - mean), 2);
        }
        Double stdDev = Math.sqrt(devSum/responses.size());

        //Standard error
        return stdDev/Math.sqrt(responses.size());
    }

    public Question chooseNextQuestion(Quiz quiz) {
        if (quiz.getResponses().isEmpty()) {
            return getQuestionWithScore(START_QUESTION_SCORE, quiz);
        } else if (quiz.getResponses().size() >= MAX_QUESTIONS) {
            return null;
        }

        if (quiz.getResponses().size() > MIN_QUESTIONS && calcError(quiz) < ERROR_LIMIT) {
            return null;
        }

        Response lastResponse = getPreviousResponse(quiz);
        Integer nextQuestionScore;
        if (lastResponse.isCorrect()) {
            nextQuestionScore = Math.min(lastResponse.getQuestion().getScore() + 1, MAX_QUESTION_SCORE);
        } else {
            nextQuestionScore = Math.max(lastResponse.getQuestion().getScore() - 1, MIN_QUESTION_SCORE);
        }
        return getQuestionWithScore(nextQuestionScore, quiz);
    }

    public Integer calcFinalScore(Quiz quiz) {
        Double mean = calcMean(quiz);
        return (int)(100*mean/MAX_QUESTION_SCORE);
    }

    public void debugLastResponse(Quiz quiz) {
        List<Response> responses = quiz.getResponses();
        Response lastResponse = responses.get(quiz.getResponses().size() - 1);

        System.out.print("Question ID: ");
        System.out.println(lastResponse.getQuestion().getId());

        System.out.print("Score: ");
        System.out.println(lastResponse.getQuestion().getScore());

        System.out.print("Corect Answers IDs:");
        for(Option option : lastResponse.getQuestion().getCorrectOptions()) {
            System.out.print(" ");
            System.out.print(option.getId());
        }
        System.out.println();

        System.out.print("Selected Answers IDs:");
        for(Option option : lastResponse.getSelectedOptions()) {
            System.out.print(" ");
            System.out.print(option.getId());
        }
        System.out.println();

        System.out.print("Is Correct: ");
        System.out.println(lastResponse.isCorrect());

        System.out.print("Current Score: ");
        System.out.println(calcMean(quiz));

        System.out.print("Current Error: ");
        System.out.println(calcError(quiz));
    }
}
