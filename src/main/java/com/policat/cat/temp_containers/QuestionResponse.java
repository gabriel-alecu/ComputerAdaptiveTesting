package com.policat.cat.temp_containers;

import com.policat.cat.entities.Answer;
import com.policat.cat.entities.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionResponse {
    private Question question;
    private Set<Answer> selectedAnswers = new HashSet<>();


    public QuestionResponse(Question question, Set<Answer> selectedAnswers) {
        this.question = question;
        this.selectedAnswers = selectedAnswers;
    }

    public Question getQuestion() {
        return question;
    }

    public Set<Answer> getSelectedAnswers() {
        return selectedAnswers;
    }
}
