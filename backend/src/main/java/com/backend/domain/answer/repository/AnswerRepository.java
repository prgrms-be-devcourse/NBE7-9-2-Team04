package com.backend.domain.answer.repository;

import com.backend.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @EntityGraph(attributePaths = {"author", "question"})
    Page<Answer> findByQuestionIdAndIsPublicTrue(Long questionId, Pageable pageable);
    @EntityGraph(attributePaths = {"author", "question"})
    Page<Answer> findByAuthorId(Long authorId, Pageable pageable);

    Optional<Answer> findByQuestionIdAndAuthorId(Long questionId, Long authorId);
}
