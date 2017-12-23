package com.policat.cat.services;

import com.policat.cat.configs.QuizConfig;
import com.policat.cat.entities.Domain;
import com.policat.cat.entities.Option;
import com.policat.cat.entities.Question;
import com.policat.cat.repositories.DomainRepository;
import com.policat.cat.repositories.QuestionRepository;
import com.policat.cat.session.Quiz;
import com.policat.cat.entities.QuestionResponse;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuizService {


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

    public Question getQuestionWithScore(Integer score, Quiz quiz) {
        List<Long> usedQuestions = new ArrayList<>();
        for (QuestionResponse response : quiz.getResponses()) {
            Question question = response.getQuestion();
            usedQuestions.add(question.getId());
        }
        List<Question> available;
        if (usedQuestions.isEmpty()) {
            available = questionRepository.findByScore(quiz.getDomain(), score);
        } else {
            available = questionRepository.findNewByScore(quiz.getDomain(), score, usedQuestions);
        }
        if (available.isEmpty()) {
            return null;
        }

        Random randGen = new Random();
        Integer chosenPos = randGen.nextInt(available.size());
        Question chosen = available.get(chosenPos);
        return getQuestionOptions(chosen);
    }

    public Integer computedResponseScore(QuestionResponse response) {
        Integer questionScore = response.getQuestion().getScore();
        if (response.isCorrect()) {
            questionScore = Math.min(++questionScore, QuizConfig.MAX_QUESTION_SCORE);
        } else {
            --questionScore;
        }
        return questionScore;
    }

    //the first NUM_SETUP_QUESTIONS responses are used for calibration, and are ignored in the scoring
    public List<QuestionResponse> getScoreResponses(Quiz quiz) {
        List<QuestionResponse> responses = quiz.getResponses();
        if (responses.size() < QuizConfig.NUM_SETUP_QUESTIONS) {
            return responses.subList(responses.size(), responses.size());
        }
        return responses.subList(QuizConfig.NUM_SETUP_QUESTIONS, responses.size());
    }

    public Double calcMean(Quiz quiz) {
        Integer sumScores = 0;
        List<QuestionResponse> responses = getScoreResponses(quiz);

        if (responses.size() == 0) {
            return 0.0;
        }

        for (QuestionResponse response : responses) {
            sumScores += computedResponseScore(response);
        }

        return sumScores / (double) responses.size();
    }

    public Double calcError(Quiz quiz) {
        //Standard deviation
        Double mean = calcMean(quiz);
        Double devSum = 0.0;
        List<QuestionResponse> responses = getScoreResponses(quiz);

        if (responses.size() == 0) {
            return 0.0;
        }

        for (QuestionResponse response : responses) {
            Integer questionScore = computedResponseScore(response);
            devSum += Math.pow((questionScore - mean), 2);
        }
        Double stdDev = Math.sqrt(devSum / responses.size());

        //Standard error
        return stdDev / Math.sqrt(responses.size());
    }

    public Question chooseNextQuestion(Quiz quiz) {
        if (quiz.getResponses().isEmpty()) {
            return getQuestionWithScore(QuizConfig.START_QUESTION_SCORE, quiz);
        } else if (quiz.getResponses().size() >= QuizConfig.MAX_QUESTIONS) {
            return null;
        }

        if (quiz.getResponses().size() > QuizConfig.MIN_QUESTIONS && calcError(quiz) < QuizConfig.ERROR_LIMIT) {
            return null;
        }

        QuestionResponse mostRecentResponse = quiz.getMostRecentResponse();
        Integer nextQuestionScore;
        if (mostRecentResponse.isCorrect()) {
            nextQuestionScore = Math.min(mostRecentResponse.getQuestion().getScore() + 1, QuizConfig.MAX_QUESTION_SCORE);
        } else {
            nextQuestionScore = Math.max(mostRecentResponse.getQuestion().getScore() - 1, QuizConfig.MIN_QUESTION_SCORE);
        }
        return getQuestionWithScore(nextQuestionScore, quiz);
    }

    public Integer calcFinalScore(Quiz quiz) {
        Double mean = calcMean(quiz);
        return (int) (100 * mean / QuizConfig.MAX_QUESTION_SCORE);
    }

    public void debugLastResponse(Quiz quiz) {
        List<QuestionResponse> responses = quiz.getResponses();
        QuestionResponse lastResponse = responses.get(quiz.getResponses().size() - 1);

        System.out.print("Question ID: ");
        System.out.println(lastResponse.getQuestion().getId());

        System.out.print("Score: ");
        System.out.println(lastResponse.getQuestion().getScore());

        System.out.print("Corect Answers IDs:");
        for (Option option : lastResponse.getQuestion().getCorrectOptions()) {
            System.out.print(" ");
            System.out.print(option.getId());
        }
        System.out.println();

        System.out.print("Selected Answers IDs:");
        for (Option option : lastResponse.getSelectedOptions()) {
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
