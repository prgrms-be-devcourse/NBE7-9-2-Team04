package com.backend.api.resume.controller;

import com.backend.api.resume.dto.request.ResumeCreateRequest;
import com.backend.api.resume.dto.request.ResumeUpdateRequest;
import com.backend.api.resume.dto.response.ResumeCreateResponse;
import com.backend.api.resume.dto.response.ResumeReadResponse;
import com.backend.api.resume.dto.response.ResumeUpdateResponse;
import com.backend.api.resume.service.ResumeService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            @Valid @PathVariable Long userId,
            @RequestBody ResumeCreateRequest request) {
        ResumeCreateResponse response = resumeService.createResume(userId, request);
        return ApiResponse.ok("이력서가 생성되었습니다.", response);
    }

    @PutMapping("/{userId}/{resumeId}")
    @Operation(summary = "이력서 수정", description = "사용자의 이력서를 수정합니다.")
    public ApiResponse<ResumeUpdateResponse> updateResume(
            @Parameter(description = "사용자 ID", example = "1")
            @Valid  @PathVariable Long userId,
            @Parameter(description = "이력서 ID", example = "1")
            @Valid @PathVariable Long resumeId,
            @RequestBody ResumeUpdateRequest request) {
        ResumeUpdateResponse response = resumeService.updateResume(userId, resumeId, request);
        return ApiResponse.ok("이력서가 수정되었습니다.", response);
    }

    @DeleteMapping("/{userId}/{resumeId}")
    @Operation(summary = "이력서 삭제", description = "사용자의 이력서를 삭제합니다.")
    public ApiResponse<Void> deleteResume(
            @Parameter(description = "사용자 ID", example = "1")
            @Valid @PathVariable Long userId,
            @Parameter(description = "이력서 ID", example = "1")
            @Valid @PathVariable Long resumeId) {
        resumeService.deleteResume(userId, resumeId);
        return ApiResponse.ok("이력서가 삭제되었습니다.", null);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "이력서 조회", description = "사용자의 이력서를 조회합니다.")
    public ApiResponse<ResumeReadResponse> getResume(
            @Parameter(description = "사용자 ID", example = "1")
            @Valid @PathVariable Long userId) {
        ResumeReadResponse response = resumeService.readResume(userId);
        return ApiResponse.ok("이력서를 조회했습니다.", response);
    }
}
