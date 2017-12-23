package com.policat.cat.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class QuizResult {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Domain domain;

    @OneToMany(mappedBy = "quizResult", cascade = CascadeType.ALL)
    private List<QuestionResponse> questionResponses = new ArrayList<>();

    public QuizResult() {
    }

    public QuizResult(Integer score, User user, Domain domain, List<QuestionResponse> questionResponses) {
        Calendar calendar = Calendar.getInstance();
        this.date = calendar.getTime();
        this.score = score;
        this.user = user;
        this.domain = domain;
        this.questionResponses = questionResponses;

        for(QuestionResponse questionResponse : this.questionResponses) {
            questionResponse.setQuizResult(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<QuestionResponse> getQuestionResponses() {
        return questionResponses;
    }

    public void setQuestionResponses(List<QuestionResponse> questionResponses) {
        this.questionResponses = questionResponses;
    }
}
