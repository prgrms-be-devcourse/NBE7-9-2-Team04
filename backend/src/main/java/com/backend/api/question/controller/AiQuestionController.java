package com.backend.api.question.controller;

import com.backend.api.question.dto.response.AiQuestionResponse;
import com.backend.api.question.service.OpenAiService;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai/questions")
@RequiredArgsConstructor
@Tag(name = "AI Questions", description = "AI 관련 질문 관리 API")
public class AiQuestionController {
    private final OpenAiService openAiService;

    private final Rq rq;

    @PostMapping("")
    @Operation(summary = "AI 면접 질문 생성", description = "AI 질문을 생성합니다.")
    public ApiResponse<List<AiQuestionResponse>> createAiQuestion() throws JsonProcessingException {
        Long userId = rq.getUser().getId();
        List<AiQuestionResponse> responses = openAiService.createAiQuestion(userId);
        return ApiResponse.created("AI 면접 질문이 완료되었습니다.",responses);
    }
}
