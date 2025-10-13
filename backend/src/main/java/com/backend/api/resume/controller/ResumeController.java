package com.backend.api.resume.controller;

import com.backend.api.resume.dto.response.ResumeCreateResponse;
import com.backend.api.resume.service.ResumeService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/resumes")
@RequiredArgsConstructor
@Tag(name = "Resumes", description = "이력서 관련 API")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/{userId}")
    @Operation(summary = "이력서 생성", description = "사용자의 이력서를 생성합니다.")
    public ApiResponse<ResumeCreateResponse> createResume(@PathVariable Long userId) {
        ResumeCreateResponse response = resumeService.createResume(userId);
        return ApiResponse.ok("이력서가 생성되었습니다.", response);
    }
}
