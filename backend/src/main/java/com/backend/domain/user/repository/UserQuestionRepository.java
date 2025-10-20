package com.backend.domain.user.repository;

import com.backend.domain.user.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    List<UserQuestion> findByUserId(Long userId);
}
