package com.backend.domain.user.entity;

import com.backend.domain.question.entity.Question;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

//user_id, question_id를 FK키로 가지는 테이블
@Entity
@Getter
@NoArgsConstructor
public class UserQuestion extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question")
    private Question question;

    private int score;


}