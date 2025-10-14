package com.backend.api.question.controller;

import com.backend.api.question.dto.request.AdminQuestionAddRequest;
import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.api.question.dto.response.QuestionResponse;
import com.backend.api.question.service.AdminQuestionService;
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
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@Tag(name = "Admin Questions", description = "관리자 질문 관리 API")
public class AdminQuestionController {
    private final AdminQuestionService adminQuestionService;

    @PostMapping
    @Operation(summary = "질문 생성 (관리자)", description = "관리자가 새로운 질문을 직접 등록합니다.")
    public ResponseEntity<ApiResponse<QuestionResponse>> addQuestion(
            @Valid @RequestBody AdminQuestionAddRequest request) {
        QuestionResponse response = adminQuestionService.addQuestion(request);
        return ResponseEntity.ok(ApiResponse.ok("질문이 생성되었습니다.", response));
    }
}
