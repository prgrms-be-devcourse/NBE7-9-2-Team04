package com.backend.api.post.dto.request;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


//게시글 생성
public record PostAddRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @NotNull(message = "마감일은 필수입니다.")
        LocalDateTime deadline,

        @NotNull(message = "진행 상태는 필수입니다.")
        PostStatus status,

        @NotNull(message = "상단 고정 여부는 필수입니다.")
        PinStatus pinStatus
) {}
