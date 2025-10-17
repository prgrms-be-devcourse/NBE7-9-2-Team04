package com.backend.api.post.dto.request;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


@Schema(description = "게시글 생성 요청 DTO")
public record PostAddRequest(
        @Schema(description = "게시글 제목", example = "스프링 부트 프로젝트 팀원 구합니다.")
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
        String title,

        @Schema(description = "게시글 내용", example = "백엔드 2명, 프론트엔드 2명 구합니다. 주제는...")
        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @Schema(description = "마감일", example = "2025-12-31T23:59:59")
        @NotNull(message = "마감일은 필수입니다.")
        LocalDateTime deadline,

        @Schema(description = "진행 상태", example = "ING")
        @NotNull(message = "진행 상태는 필수입니다.")
        PostStatus status,

        @Schema(description = "상단 고정 여부", example = "NOT_PINNED")
        @NotNull(message = "상단 고정 여부는 필수입니다.")
        PinStatus pinStatus
) {}
