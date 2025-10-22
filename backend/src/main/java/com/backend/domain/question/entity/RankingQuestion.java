package com.backend.domain.question.entity;

import com.backend.domain.question.entity.Question;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RankingQuestion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title", nullable = false)
    private Question question;

    @Column(nullable = false)
    private Long ai_score;

    public void updateScore(Long ai_score) {
        this.ai_score = ai_score;
    }
}
