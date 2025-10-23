package com.backend.domain.ranking.entity;

import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Ranking extends BaseEntity {

    @Column(nullable = false)
    private Integer totalScore;

    @Column(nullable = false)
    private String tier;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
