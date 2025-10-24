package com.backend.domain.question.repository;

import com.backend.domain.question.entity.Question;
import com.backend.domain.question.entity.QuestionCategoryType;
import com.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @EntityGraph(attributePaths = {"author"})
    Page<Question> findByIsApprovedTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"author"})
    Page<Question> findByCategoryTypeAndIsApprovedTrue(QuestionCategoryType categoryType, Pageable pageable);

    int CountByAuthor(User author);
}
