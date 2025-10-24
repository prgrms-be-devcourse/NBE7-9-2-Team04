package com.backend.domain.review.entity;

import com.backend.domain.resume.entity.Resume;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Review extends BaseEntity {

    @Column(name = "reviewId")
    private Long reviewId;

    @Column(name = "AiReviewContent", columnDefinition = "TEXT", nullable = false)
    private String AiReviewContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resumeId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

}