package com.policat.cat.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
class Question {
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private Integer score;

    @ManyToOne
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    private Collection<Answer> answers = new ArrayList<>();


    public Question() {}

    public Question(String text, Integer score) {
        this.text = text;
        this.score = score;
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
}
