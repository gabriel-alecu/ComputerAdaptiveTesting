package com.policat.cat.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class QuizResult {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)   //float to fix validation bug, Oracle DB's floats are big enough anyway...
    private Integer score;

    @Column(name="`DATE`", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name="`USER_ID`", nullable = false)   //fix for name being generated lowercase, due to the user table fix
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Quiz quiz;


    public QuizResult() {}

    public QuizResult(Integer score, User user, Quiz quiz) {
        Calendar calendar = Calendar.getInstance();
        this.date = calendar.getTime();

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
