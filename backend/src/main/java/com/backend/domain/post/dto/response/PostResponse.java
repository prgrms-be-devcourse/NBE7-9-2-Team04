package com.backend.domain.post.dto.response;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;

import java.time.LocalDateTime;
import java.util.Optional;

// 게시글 응답
public record PostResponse(
        Long postId,
        String title,
        String content,
        LocalDateTime deadline,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        PostStatus status,
        PinStatus pinStatus,
        String nickName // 작성자의 닉네임
) {

    public static PostResponse from(Post post) {

        String nickName = Optional.ofNullable(post.getUsers())
                .map(user -> user.getNickname())
                .orElseThrow(() -> new IllegalStateException("게시글의 작성자 정보가 누락되었습니다."));

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