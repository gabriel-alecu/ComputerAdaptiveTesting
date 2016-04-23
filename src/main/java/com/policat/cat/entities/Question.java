package com.policat.cat.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Integer score;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();


    public Question() {}

    public Question(String text, Integer score, Quiz quiz) {
        this.text = text;
        this.score = score;
        this.quiz = quiz;
    }

    public Question(String text, Integer score, Quiz quiz, List<Answer> answers) {
        this.text = text;
        this.score = score;
        this.quiz = quiz;
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Transient
    public Set<Answer> getCorrectAnswers() {
        Set<Answer> correctAnswers = new HashSet<>();
        for(Answer answer : answers) {
            if(answer.getCorrect()) {
                correctAnswers.add(answer);
            }
        }
        return correctAnswers;
    }
}
