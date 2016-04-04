package com.policat.cat.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
class Question {
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
    private Collection<Answer> answers = new ArrayList<>();


    public Question() {}

    public Question(String text, Integer score, Quiz quiz) {
        this.text = text;
        this.score = score;
        this.quiz = quiz;
    }

    public Question(String text, Integer score, Quiz quiz, Collection<Answer> answers) {
        this.text = text;
        this.score = score;
        this.quiz = quiz;
        this.answers = answers;
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

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<Answer> answers) {
        this.answers = answers;
    }
}
