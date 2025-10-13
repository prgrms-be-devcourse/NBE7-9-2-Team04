package com.backend.domain.resume.entity;

import com.backend.domain.user.entity.Users;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Resume extends BaseEntity {

    @NotNull
    private String content;  // 이력서 내용

    @NotNull
    private String skill;   // 기술 스택

    @NotNull
    private String activity;  // 대외 활동

    @NotNull
    private String certification;  // 자격증

    @NotNull
    private String career;  // 경력 사항

    @NotNull
    private String portfolio_url; // 포트폴리오 URL

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;  // 이력서 소유자
}
