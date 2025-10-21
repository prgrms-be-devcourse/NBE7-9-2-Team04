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

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private QuestionCategoryType categoryType;
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true)
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public Question(String title, String content, User author, QuestionCategoryType categoryType) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.categoryType = categoryType;
        this.isApproved = false;
        this.score = 0;
    }

    public void updateApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void updateScore(Integer newScore) { this.score = newScore; }

    public void updateUserQuestion(String title, String content, QuestionCategoryType categoryType) {
        this.title = title;
        this.content = content;
        this.categoryType = categoryType;
    }

    public void updateAdminQuestion(String title, String content, Boolean isApproved, Integer score, QuestionCategoryType categoryType) {
        this.title = title;
        this.content = content;
        this.categoryType = categoryType;
        updateApproved(isApproved);
        updateScore(score);
    }

    public void changeCategory(QuestionCategoryType categoryType) {
        this.categoryType = categoryType;
    }

}