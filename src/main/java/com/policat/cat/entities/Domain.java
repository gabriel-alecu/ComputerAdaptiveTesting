package com.policat.cat.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Domain implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<QuizResult> quizResults = new ArrayList<>();


    public Domain() {}

    public Domain(String name) {
        this.name = name;
    }

    public Domain(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<QuizResult> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(List<QuizResult> quizResults) {
        this.quizResults = quizResults;
    }

    @Transient
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    @Transient
    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }
}
