package com.backend.domain.answer.entity;

import com.backend.domain.question.entity.Question;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Answer extends BaseEntity {

    @Column(nullable = false)
    private String content;

    private Integer aiScore;

    @Column(nullable = false)
    private boolean isPublic;

    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(nullable = false)
    private Question question;

    public void update(String content, Boolean isPublic) {
        if(content != null && !content.isBlank()) {
            this.content = content;
        }
        if(isPublic != null) {
            this.isPublic = isPublic;
        }
    }

}
