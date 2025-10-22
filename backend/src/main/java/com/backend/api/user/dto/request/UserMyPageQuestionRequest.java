package com.backend.api.user.dto.request;

import com.backend.domain.answer.entity.Answer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserMyPageQuestionRequest {
    @Schema(description = "유저 ID")
    private Long userId;

    @Schema(description = "문제 번호")
    private String questionId;

    @Schema(description = "문제 제목")
    private String title;

    @Schema(description = "AI 채점 점수")
    private Integer aiScore;

    @Schema(description = "답변 수정일")
    private LocalDateTime modifyDate;

    public static UserMyPageQuestionRequest fromEntity(Answer answer) {
        return UserMyPageQuestionRequest.builder()
                .userId(answer.getAuthor().getId())
                .questionId(answer.getQuestion().getQuestionId())
                .title(answer.getQuestion().getTitle())
                .aiScore(answer.getAiScore())
                .modifyDate(answer.getModifyDate())
                .build();
    }
}
