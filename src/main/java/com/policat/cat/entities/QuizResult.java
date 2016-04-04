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

    public QuizResult(Double score, User user, Quiz quiz) {
        this.score = score;
        this.user = user;
        this.quiz = quiz;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
