package com.policat.cat.temp_containers;

import com.policat.cat.entities.Domain;

import javax.validation.constraints.NotNull;

public class QuizRequestDTO {
    @NotNull
    private Domain domain;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
