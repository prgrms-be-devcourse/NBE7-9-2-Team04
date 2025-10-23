package com.backend.domain.userQuestion.repository;

import com.backend.domain.question.entity.Question;
import com.backend.domain.user.entity.User;
import com.backend.domain.userQuestion.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {

    Optional<UserQuestion> findByUserAndQuestion(User user, Question question);
    List<UserQuestion> findByUser_Id(Long userId);
    boolean existsByUserAndQuestion(User user, Question question);
}
