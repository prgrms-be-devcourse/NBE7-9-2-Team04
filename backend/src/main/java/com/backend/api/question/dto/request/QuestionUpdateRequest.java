package com.backend.api.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuestionUpdateRequest(
        @NotBlank(message = "질문 제목은 필수입니다.")
        @Schema(description = "질문 제목", example = "수정할 제목")
        String title,

        @NotBlank(message = "질문 내용은 필수입니다.")
        @Schema(description = "질문 내용", example = "수정할 내용")
        String content,

        @Schema(description = "카테고리 ID 목록", example = "[1, 4]")
        List<Long> categoryIds
) {}
