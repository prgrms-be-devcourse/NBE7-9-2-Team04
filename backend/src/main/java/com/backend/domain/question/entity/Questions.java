package com.backend.domain.question.entity;

import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="questions")
@Getter
@NoArgsConstructor
public class Questions extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true)
    private Boolean isApproved = false; // 0: 미승인, 1: 승인

    @Column(nullable = false)
    private Integer score = 0; // 기본값 0

    @Builder
    public Questions(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
