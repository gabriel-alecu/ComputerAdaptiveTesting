package com.policat.cat.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "[option]")
public class Option {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Boolean correct = false;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Question question;

    @ManyToMany(mappedBy="selectedOptions")
    private List<QuestionResponse> questionResponses;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<QuestionResponse> getQuestionResponses() {
        return questionResponses;
    }

    public void setQuestionResponse(List<QuestionResponse> questionResponses) {
        this.questionResponses = questionResponses;
    }
}