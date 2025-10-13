package com.backend.domain.question.repository;

import com.backend.domain.question.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Questions,Integer> {

}
