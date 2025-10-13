package com.backend.api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record CommentCreateRequestDto(
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        @Size(min = 1, max = 500, message = "댓글 내용은 1자 이상 500자 이하로 입력해주세요.")
        String content
) {}