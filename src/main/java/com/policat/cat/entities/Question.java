package com.policat.cat.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String text;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer score;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Domain domain;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>();


    public Question() {
    }

    public Question(String text, Integer score, Domain domain) {
        this.text = text;
        this.score = score;
        this.domain = domain;
    }

    public Question(String text, Integer score, Domain domain, List<Option> options) {
        this.text = text;
        this.score = score;
        this.domain = domain;
        this.options = options;
    }

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Transient
    public void addOption(Option option) {
        this.options.add(option);
    }

    @Transient
    public void removeOption(Option option) {
        this.options.remove(option);
    }

    @Transient
    public Set<Option> getCorrectOptions() {
        Set<Option> correctOptions = new HashSet<>();
        for (Option option : options) {
            if (option.getCorrect()) {
                correctOptions.add(option);
            }
        }
        return correctOptions;
    }
}
