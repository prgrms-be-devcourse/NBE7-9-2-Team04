package com.backend.api.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AiReviewResponse(

        @Schema(description = "포트폴리오 AI 첨삭 (Markdown 형식)",
                example = "## 포트폴리오 분석 결과...")
        String feedbackContent
) {
    public static AiReviewResponse of(String feedbackContent) {
        return new AiReviewResponse(feedbackContent);
    }
}
