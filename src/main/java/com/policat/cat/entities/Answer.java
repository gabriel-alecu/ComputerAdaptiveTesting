package com.policat.cat.entities;

import javax.persistence.*;

@Entity
class Answer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Question question;


    public Answer() {}

    public Answer(String text, Boolean isCorrect, Question question) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.question = question;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}