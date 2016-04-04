package com.policat.cat.entities;

import javax.persistence.*;

@Entity
class QuizResult {
    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "FLOAT")   //bug fix, Oracle DB's floats are big enough anyway
    private Double score;

    @JoinColumn(name="`USER_ID`")   //fix for being lowercase, due to the user table fix
    @ManyToOne
    private User user;

    @ManyToOne
    private Quiz quiz;


    public QuizResult() {}

    public QuizResult(Double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
