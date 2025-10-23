package com.backend.api.resume.controller;

import com.backend.api.question.service.OpenAiService;
import com.backend.api.resume.dto.response.ResumeAiFeedbackResponse;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/portfolio-review")
@RequiredArgsConstructor
@Tag(name = "Portfolio Ai Feedback", description = "포트폴리오 AI 첨삭")
public class ResumeAiFeedbackController {

    private final OpenAiService openAiService;

    private final Rq rq;

    @PostMapping
    @Operation(summary = "포트폴리오 AI 첨삭 생성", description = "사용자의 포트폴리오를 바탕으로 AI 첨삭을 생성합니다.")
    public ApiResponse<ResumeAiFeedbackResponse> createResumeFeedback() throws JsonProcessingException {
        Long userId = rq.getUser().getId();
        ResumeAiFeedbackResponse response = openAiService.createResumeAiFeedback(userId);
        return ApiResponse.created("포트폴리오 AI 첨삭이 완료되었습니다.", response);
    }
}
