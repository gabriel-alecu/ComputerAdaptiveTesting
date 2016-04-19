package com.policat.cat.temp_containers;

import com.policat.cat.entities.Quiz;

import javax.validation.constraints.NotNull;

public class QuizRequestDTO {
    @NotNull
    private Quiz quiz;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
