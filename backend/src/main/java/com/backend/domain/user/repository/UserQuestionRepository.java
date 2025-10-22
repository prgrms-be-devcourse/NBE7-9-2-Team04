package com.backend.domain.user.repository;

import com.backend.domain.user.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {

    List<UserQuestion> findByUser_Id(Long userId);

    Optional<UserQuestion> findByUser_IdAndQuestion_Id(Long userId, Long questionId);
}
