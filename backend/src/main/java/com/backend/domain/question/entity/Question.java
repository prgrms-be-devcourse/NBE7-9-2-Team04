package com.backend.domain.question.entity;

import com.backend.domain.answer.entity.Answer;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true)
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public Question(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void updateApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void updateScore(Integer newScore) { this.score = newScore; }

    public void updateUserQuestion(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateAdminQuestion(String title, String content, Boolean isApproved, Integer score) {
        this.title = title;
        this.content = content;

        if(isApproved != null) {
            this.isApproved = isApproved;
        }

        if(score != null) {
            updateScore(score);
        }
    }

}