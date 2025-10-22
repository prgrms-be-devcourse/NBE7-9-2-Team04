package com.backend.domain.user.entity;

import com.backend.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "유저", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "문제", nullable = false)
    private Question question;

    @Column(nullable = false)
    private Integer aiScore;

    private LocalDateTime modifyDate;

    public void updateScore(Integer aiScore) {
        if (aiScore != null) {
            this.aiScore = aiScore;
            this.modifyDate = LocalDateTime.now();
        }
    }

    @Repository
    public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
        List<UserQuestion> findByUser_Id(Long userId);
    }

}