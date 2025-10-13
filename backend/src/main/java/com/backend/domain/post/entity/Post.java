package com.backend.domain.post.entity;

import com.backend.domain.user.entity.Users;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post")


public class Post extends BaseEntity {

    public enum PostStatus {
        ING, // 진행중
        CLOSED // 마감
    }

    public enum PinStatus {
        PINNED, // 상단 고정
        NOT_PINNED  // 상단 고정X
    }

    @NotNull
    private String title; // 제목

    @NotNull
    private String content; // 내용

    @NotNull
    private LocalDateTime deadline; // 마감일

    @NotNull
    private PostStatus status; // 진행상태

    @NotNull
    private PinStatus pinStatus; // 상단 고정 여부

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // userId와 매핑
    private Users users;  // 게시글 작성자 ID
}
