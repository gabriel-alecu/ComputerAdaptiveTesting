package com.policat.cat.entities;

import com.policat.cat.entities.Option;
import com.policat.cat.entities.Question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class QuestionResponse {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(nullable = false)
    private QuizResult quizResult;

    @ManyToMany
    @JoinTable(joinColumns = {@JoinColumn(name = "question_response_id")}, inverseJoinColumns = {@JoinColumn(name = "selected_option_id")})
    private Set<Option> selectedOptions = new HashSet<>();

    public QuestionResponse() {
    }

    public QuestionResponse(Question question, Set<Option> selectedOptions) {
        this.question = question;
        this.selectedOptions = selectedOptions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuizResult getQuizResult() {
        return quizResult;
    }

    public void setQuizResult(QuizResult quizResult) {
        this.quizResult = quizResult;
    }

    public Set<Option> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(Set<Option> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    @Transient
    public Boolean isCorrect() {
        Set<Option> correctOptions = this.question.getCorrectOptions();
        Set<Option> userOptions = this.selectedOptions;
        return correctOptions.equals(userOptions);
    }
}
