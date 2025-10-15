package com.backend.domain.post.entity;

import com.backend.domain.comment.entity.Comment;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post")


public class Post extends BaseEntity {


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
    private User users;  // 게시글 작성자 ID

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comments;

    public void updatePost(String title, String content, LocalDateTime deadline, PostStatus status, PinStatus pinStatus) {
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.status = status;
        this.pinStatus = pinStatus;
    }
}
