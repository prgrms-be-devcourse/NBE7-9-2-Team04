package com.backend.domain.question.entity;

import com.backend.domain.answer.entity.Answer;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Question extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String questionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true)
    @Builder.Default
    private Boolean isApproved = false; // 0: 미승인, 1: 승인

    @Column(nullable = false)
    @Builder.Default
    private Integer score = 0; // 기본값 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;


    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private QuestionCategoryType categoryType;
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Answer> answers;



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