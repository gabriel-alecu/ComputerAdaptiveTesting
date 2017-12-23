package com.policat.cat.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
public class QuestionTag {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private URL link;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Domain domain;

    @OneToMany(mappedBy = "questionTag", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

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

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
