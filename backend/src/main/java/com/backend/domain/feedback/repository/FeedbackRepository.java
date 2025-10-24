package com.backend.domain.feedback.repository;

import com.backend.domain.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    Optional<Feedback> findByAnswerId(Long answerId);
}
