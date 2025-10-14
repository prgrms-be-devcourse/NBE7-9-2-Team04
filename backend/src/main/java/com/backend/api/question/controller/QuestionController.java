package com.backend.api.question.controller;

import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.api.question.dto.response.QuestionResponse;
import com.backend.api.question.service.QuestionService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Tag(name = "Questions", description = "사용자 질문 관련 API")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "질문 생성", description = "사용자가 질문을 생성합니다.")
    public ResponseEntity<ApiResponse<QuestionResponse>> addQuestion(
            @Valid @RequestBody QuestionAddRequest request) {
        QuestionResponse response = questionService.addQuestion(request);
        return ResponseEntity.ok(ApiResponse.ok("질문이 생성되었습니다.",response));
    }
}
