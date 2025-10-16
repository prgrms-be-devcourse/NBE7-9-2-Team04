package com.backend.api.post.dto.response;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Optional;

// 게시글 응답
    public record PostResponse(
            @Schema(description = "게시글 ID", example = "1")
            Long postId,

            @Schema(description = "제목", example = "첫번째 게시물")
            String title,

            @Schema(description = "내용", example = "함께 팀 프로젝트를 진행할 백엔드 개발자 구합니다.")
            String content,

            @Schema(description = "마감일", example = "2025-10-25T17:00:00")
            LocalDateTime deadline,

            @Schema(description = "작성일", example = "2025-10-18T10:30:00")
            LocalDateTime createDate,

            @Schema(description = "수정일", example = "2025-10-18T10:50:00")
            LocalDateTime modifyDate,

            @Schema(description = "진행 상태", example = "IN_PROGRESS", allowableValues = {"ING", "CLOSED"})
            PostStatus status,

            @Schema(description = "상단 고정 여부", example = "UNPINNED", allowableValues = {"PINNED", "NOT_PINNED"})
            PinStatus pinStatus,

            @Schema(description = "작성자의 닉네임", example = "개발자A")
            String nickName // 작성자의 닉네임
    ) {

    public static PostResponse from(Post post) {

        String nickName = Optional.ofNullable(post.getUsers())
                .map(user -> user.getNickname())
                .filter(name -> !name.isEmpty()) // 닉네임이 비어있는 문자열("")인 경우도
                .orElseThrow(() ->
                        new ErrorException(ErrorCode.NOT_FOUND_NICKNAME)
                );


        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getDeadline(),
                post.getCreateDate(),
                post.getModifyDate(),
                post.getStatus(),
                post.getPinStatus(),
                nickName
        );
    }
}