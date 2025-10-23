package com.backend.domain.userQuestion.repository;

import com.backend.domain.userQuestion.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
}
