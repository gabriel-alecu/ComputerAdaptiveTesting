package com.policat.cat.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Quiz implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "quiz")
    private Collection<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz")
    private Collection<QuizResult> quizResults = new ArrayList<>();


    public Quiz() {}

    public Quiz(String name) {
        this.name = name;
    }

    public Quiz(String name, Collection<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
    }

    public Collection<QuizResult> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(Collection<QuizResult> quizResults) {
        this.quizResults = quizResults;
    }
}
