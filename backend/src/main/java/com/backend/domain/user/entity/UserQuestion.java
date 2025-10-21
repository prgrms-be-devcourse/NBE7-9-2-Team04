package com.backend.domain.user.entity;

import com.backend.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "문제 제목")
    private Question question;
    private LocalDateTime modifyDate;
}