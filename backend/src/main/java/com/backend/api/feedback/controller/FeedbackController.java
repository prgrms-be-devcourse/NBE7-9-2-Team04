package com.backend.api.feedback.controller;

import com.backend.api.feedback.dto.response.FeedbackReadResponse;
import com.backend.api.feedback.service.FeedbackService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
@Tag(name = "FeedbackController", description = "면접 답변 피드백 API")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("{feedbackId}")
    @Operation(summary = "피드백 단건 조회", description = "답변 피드백 단건 조회합니다.")
    public ApiResponse<FeedbackReadResponse> getAdminPayment(
            @PathVariable Long feedbackId
    ) {
        FeedbackReadResponse response = feedbackService.readFeedback(feedbackId);
        return ApiResponse.ok("피드백 단건 조회합니다.", response);
    }
}
