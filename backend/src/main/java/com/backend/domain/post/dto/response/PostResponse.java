package com.backend.domain.post.dto.response;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
//게시글 응답
public class PostResponse {
    
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime deadline;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private PostStatus status;
    private PinStatus pinStatus;
    private String nickName; // 작성자의 닉네임 (User 엔티티에서 추출)


    public static PostResponse from(Post post) {
        String nickName = post.getUsers() != null ? post.getUsers().getNickname() : "알 수 없음";

        return PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .deadline(post.getDeadline())
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .status(post.getStatus())
                .pinStatus(post.getPinStatus())
                .nickName(nickName)
                .build();
    }
}