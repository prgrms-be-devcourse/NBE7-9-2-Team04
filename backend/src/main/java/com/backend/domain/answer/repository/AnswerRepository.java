package com.backend.domain.answer.repository;

import com.backend.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findByQuestionIdAndIsPublicTrue(Pageable pageable, Long questionId);
    Page<Answer> findByAuthorId(Pageable pageable, Long authorId);
}
