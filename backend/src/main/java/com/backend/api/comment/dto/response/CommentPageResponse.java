package com.backend.api.comment.dto.response;

import com.backend.domain.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record CommentPageResponse<T>(
        @Schema(description = "댓글 응답DTO 리스트")
        List<T> comments,
        @Schema(description = "현재 페이지 번호", example = "3")
        int currentPage,
        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages
) {
    public CommentPageResponse(Page<Comment> page, List<T> comments) {
        this(
            comments,
            page.getNumber() + 1,
            page.getTotalPages()
        );
    }
}
