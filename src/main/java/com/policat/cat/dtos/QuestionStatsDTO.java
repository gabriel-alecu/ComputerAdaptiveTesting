package com.policat.cat.dtos;

import com.policat.cat.entities.Question;

public class QuestionStatsDTO {
    private Question question;
    private Long numOptions;
    private Long numCorrectOptions;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long getNumOptions() {
        return numOptions;
    }

    public void setNumOptions(Long numOptions) {
        this.numOptions = numOptions;
    }

    public Long getNumCorrectOptions() {
        return numCorrectOptions;
    }

    public void setNumCorrectOptions(Long numCorrectOptions) {
        this.numCorrectOptions = numCorrectOptions;
    }
}
