package com.backend.domain.question.entity;

import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Question extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true)
    private Boolean isApproved = false; // 0: 미승인, 1: 승인

    @Column(nullable = false)
    private Integer score = 0; // 기본값 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Builder
    public Question(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void setApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void setScore(Integer score) {
        if(score < 0) {
            throw new ErrorException(ErrorCode.INVALID_QUESTION_SCORE);
        }
        this.score = score;
    }

    public void updateUserQuestion(String title, String content) {
        validateTitleAndContent(title, content);

        this.title = title;
        this.content = content;
    }

    public void updateAdminQuestion(String title, String content, Boolean isApproved, Integer score) {
        validateTitleAndContent(title, content);

        this.title = title;
        this.content = content;

        if(isApproved != null) {
            this.isApproved = isApproved;
        }

        if(score != null) {
            setScore(score);
        }
    }

    private void validateTitleAndContent(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new ErrorException(ErrorCode.QUESTION_TITLE_NOT_BLANK);
        }
        if (content == null || content.isBlank()) {
            throw new ErrorException(ErrorCode.QUESTION_CONTENT_NOT_BLANK);
        }
    }
}