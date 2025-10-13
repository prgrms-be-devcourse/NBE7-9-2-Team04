package com.backend.domain.post.dto.response;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;

import java.time.LocalDateTime;

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

        String nickName = post.getUsers() != null ? post.getUsers().getNickname() : "알 수 없음";

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