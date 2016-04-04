package com.policat.cat.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="`USER`")   //need this because it's a reserved word
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private Boolean isAdmin;

    @OneToMany(mappedBy = "user")
    private Collection<QuizResult> quizResults = new ArrayList<>();


    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    public User(String username, String password, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Collection<QuizResult> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(Collection<QuizResult> quizResults) {
        this.quizResults = quizResults;
    }
}
