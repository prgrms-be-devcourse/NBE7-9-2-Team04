package com.backend.api.post.dto.response;

import com.backend.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record PostPageResponse<T>(
        @Schema(description = "게시물 응답DTO 리스트")
        List<T> posts,
        @Schema(description = "현재 페이지 번호", example = "3")
        int currentPage,
        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages
) {
    public static <T> PostPageResponse<T> from(Page<Post> page, List<T> posts) {
        return new PostPageResponse<>(
                posts,
                page.getNumber() + 1,
                page.getTotalPages()
        );
    }
}