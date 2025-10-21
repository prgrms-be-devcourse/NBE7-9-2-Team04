package com.backend.domain.question.repository;

import com.backend.domain.question.entity.Question;
import com.backend.domain.question.entity.QuestionCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByIsApprovedTrue();

    List<Question> findByCategoryTypeAndIsApprovedTrue(QuestionCategoryType categoryType);
}
