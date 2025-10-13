package com.backend.api.post.dto.response;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;

import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;


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