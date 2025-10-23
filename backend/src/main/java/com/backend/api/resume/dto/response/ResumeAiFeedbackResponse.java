package com.backend.api.resume.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResumeAiFeedbackResponse(

        @Schema(description = "포트폴리오 AI 첨삭 (Markdown 형식)",
                example = "## 포트폴리오 분석 결과...")
        String feedbackContent
) {
    public static ResumeAiFeedbackResponse of(String feedbackContent) {
        return new ResumeAiFeedbackResponse(feedbackContent);
    }
}
