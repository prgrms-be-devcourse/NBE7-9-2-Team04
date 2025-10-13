package com.backend.domain.post.dto.request;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
//게시글 수정
public class PostUpdateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "마감일은 필수입니다.")
    private LocalDateTime deadline;

    @NotNull(message = "진행 상태는 필수입니다.")
    private PostStatus status;

    @NotNull(message = "상단 고정 여부는 필수입니다.")
    private PinStatus pinStatus;
}