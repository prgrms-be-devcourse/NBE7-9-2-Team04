package com.backend.api.question.dto.response;

import com.backend.domain.question.entity.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record QuestionPageResponse<T>(
        @Schema(description = "질문 응답DTO 리스트")
        List<T> questions,
        @Schema(description = "현재 페이지 번호", example = "3")
        int currentPage,
        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages
) {

    public static <T> QuestionPageResponse<T> from(Page<Question> page, List<T> questions) {
        return new QuestionPageResponse<>(
                questions,
                page.getNumber() + 1,
                page.getTotalPages()
        );
    }
}