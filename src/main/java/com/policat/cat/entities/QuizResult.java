package com.policat.cat.entities;

import javax.persistence.*;

@Entity
public class QuizResult {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)   //float to fix validation bug, Oracle DB's floats are big enough anyway...
    private Integer score;

    @ManyToOne
    @JoinColumn(name="`USER_ID`", nullable = false)   //fix for name being generated lowercase, due to the user table fix
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Quiz quiz;


    public QuizResult() {}

    public QuizResult(Integer score, User user, Quiz quiz) {
        this.score = score;
        this.user = user;
        this.quiz = quiz;
    }

    public Long getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
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
