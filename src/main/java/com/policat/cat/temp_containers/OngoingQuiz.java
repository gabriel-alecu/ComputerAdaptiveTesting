package com.policat.cat.temp_containers;

import com.policat.cat.entities.Question;
import com.policat.cat.entities.Quiz;
import com.policat.cat.services.QuizService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Component
@Scope("session")
public class OngoingQuiz {
    private Date endTime;
    private Quiz quiz;
    private Question currentQuestion;
    private ArrayList<Long> currentSelectedAnswers = new ArrayList<>();
    private Boolean completed = false;
    private ArrayList<QuestionResponse> questionsResponses = new ArrayList<>();

    public OngoingQuiz() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, QuizService.MAX_TIME);
        this.endTime = calendar.getTime();
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
        this.currentSelectedAnswers = new ArrayList<>();
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public ArrayList<QuestionResponse> getQuestionsResponses() {
        return questionsResponses;
    }

    public void setQuestionsResponses(ArrayList<QuestionResponse> questionsResponses) {
        this.questionsResponses = questionsResponses;
    }

    public void addQuestionResponse(QuestionResponse questionResponse) {
        this.questionsResponses.add(questionResponse);
    }

    public ArrayList<Long> getCurrentSelectedAnswers() {
        //workaround for null when no answers are selected
        if(this.currentSelectedAnswers == null) {
            this.currentSelectedAnswers = new ArrayList<>();
        }
        return this.currentSelectedAnswers;
    }

    public void setCurrentSelectedAnswers(ArrayList<Long> currentSelectedAnswers) {
        this.currentSelectedAnswers = currentSelectedAnswers;
    }
}