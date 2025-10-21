package com.backend.domain.answer.repository;

import com.backend.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findByQuestionIdAndIsPublicTrueOrderByCreateDateDesc(Pageable pageable, Long questionId);
    Page<Answer> findByAuthorIdOrderByCreateDateDesc(Pageable pageable, Long authorId);
}
