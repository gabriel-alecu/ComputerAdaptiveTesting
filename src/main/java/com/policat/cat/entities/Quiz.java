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

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "quiz")
    private Collection<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz")
    private Collection<QuizResult> quizResults = new ArrayList<>();


    public Quiz() {}

    public Quiz(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
