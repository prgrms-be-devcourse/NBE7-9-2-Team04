package com.backend.api.resume.controller;

import com.backend.api.resume.dto.request.ResumeCreateRequest;
import com.backend.api.resume.dto.response.ResumeCreateResponse;
import com.backend.api.resume.service.ResumeService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/resumes")
@RequiredArgsConstructor
@Tag(name = "Resumes", description = "이력서 관련 API")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/{userId}")
    @Operation(summary = "이력서 생성", description = "사용자의 이력서를 생성합니다.")
    public ApiResponse<ResumeCreateResponse> createResume(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            @RequestBody ResumeCreateRequest request) {
        ResumeCreateResponse response = resumeService.createResume(userId, request);
        return ApiResponse.ok("이력서가 생성되었습니다.", response);
    }

}
