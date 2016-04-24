package com.policat.cat.repositories;

import com.policat.cat.entities.Domain;
import com.policat.cat.entities.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    @Query("select q from Question q where q.domain = :domain and q.score = :score")
    List<Question> findByScore(@Param("domain") Domain domain, @Param("score") Integer score);

    @Query("select q from Question q where q.domain = :domain and q.score = :score and q.id not in :used")
    List<Question> findNewByScore(@Param("domain") Domain domain, @Param("score") Integer score, @Param("used") List<Long> used);
}
