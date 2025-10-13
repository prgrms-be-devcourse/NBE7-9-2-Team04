package com.backend.api.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuestionAddRequest (
        @NotBlank(message = "질문 제목은 필수입니다.")
        @Schema(description = "질문 제목", example = "Spring Bean의 생명주기는 어떻게 되나요?")
        String title,

        @NotBlank(message = "질문 내용은 필수입니다.")
        @Schema(description = "질문 내용", example = "Bean이 생성되고 초기화되고 소멸되는 과정에 대해 설명해주세요.")
        String content,

        @Schema(description = "카테고리 ID 목록", example = "[1, 2, 3]")
        List<Long> categoryIds
) {}
