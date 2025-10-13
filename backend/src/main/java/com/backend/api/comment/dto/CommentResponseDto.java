package com.backend.api.comment.dto;

import java.time.LocalDateTime;
import com.backend.domain.comment.entity.Comment;

public record CommentResponseDto(
        Long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String content,
        Long authorId,
        String authorName,
        Long postId
) {
    public CommentResponseDto(Comment comment) {
        this(
                comment.getId(),
                comment.getCreateDate(),
                comment.getModifyDate(),
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getAuthor().getNickName(),
                comment.getPost().getId()
        );
    }
}