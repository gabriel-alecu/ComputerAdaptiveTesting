package com.policat.cat.entities;

import javax.persistence.*;

@Entity
class Answer {
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private Boolean isCorrect;

    @ManyToOne
    private Question question;


    public Answer() {}

    public Answer(String text, Boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }
}