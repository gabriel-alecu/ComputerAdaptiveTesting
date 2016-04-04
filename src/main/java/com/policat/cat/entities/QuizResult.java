package com.policat.cat.entities;

import javax.persistence.*;

@Entity
class QuizResult {
    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "FLOAT", nullable = false)   //float to fix validation bug, Oracle DB's floats are big enough anyway...
    private Double score;

    @ManyToOne
    @JoinColumn(name="`USER_ID`", nullable = false)   //fix for name being generated lowercase, due to the user table fix
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
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
