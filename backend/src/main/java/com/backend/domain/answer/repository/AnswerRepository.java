package com.backend.domain.answer.repository;

import com.backend.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndQuestionId(Long id, Long questionId);
    List<Answer> findByQuestionIdAndIsPublicTrueOrderByCreateDateDesc(Long questionId);
    List<Answer> findByAuthorIdOrderByCreateDateDesc(Long authorId);
}
