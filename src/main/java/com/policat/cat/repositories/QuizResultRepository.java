package com.policat.cat.repositories;

import com.policat.cat.entities.QuizResult;
import com.policat.cat.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizResultRepository extends CrudRepository<QuizResult, Long> {
    List<QuizResult> findByUser(User user);
}
