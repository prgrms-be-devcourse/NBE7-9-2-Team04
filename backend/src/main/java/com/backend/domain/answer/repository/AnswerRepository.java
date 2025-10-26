package com.backend.domain.answer.repository;

import com.backend.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @EntityGraph(attributePaths = {"author", "question"})
    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND a.isPublic = true ORDER BY a.feedback.aiScore DESC")
    Page<Answer> findByQuestionIdAndIsPublicTrueOrderByFeedbackScoreDesc(Long questionId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "question"})
    Page<Answer> findByAuthorId(Long authorId, Pageable pageable);

    Optional<Answer> findFirstByQuestionIdAndAuthorId(Long questionId, Long authorId);
}
