package com.backend.domain.resume.entity;

import com.backend.domain.user.entity.Users;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Resume extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String content;  // 이력서 내용

    private String skill;   // 기술 스택

    @Column(columnDefinition = "TEXT")
    private String activity;  // 대외 활동

    @Column(columnDefinition = "TEXT")
    private String certification;  // 자격증

    @Column(columnDefinition = "TEXT")
    private String career;  // 경력 사항

    private String portfolioUrl; // 포트폴리오 URL

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;  // 이력서 소유자
}
