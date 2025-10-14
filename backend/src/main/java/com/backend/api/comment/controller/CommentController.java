package com.backend.api.comment.controller;

import com.backend.api.comment.dto.request.CommentCreateRequest;
import com.backend.api.comment.dto.response.CommentResponse;
import com.backend.api.comment.service.CommentService;
import com.backend.api.post.service.PostService;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.post.entity.Post;
import com.backend.domain.user.entity.User;
import com.backend.global.dto.response.ApiResponse;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "CommentController", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/{postId}/comments")
    @Operation(summary = "댓글 작성")
    public ApiResponse<CommentResponse> createItem(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequest reqBody
    ) {

        User currentUser = rq.getCurrentUser(); // rq 추가 필요

        Post post = postService.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.POST_NOT_FOUND)); // postService.findById() 추가 필요

        Comment newComment = commentService.writeComment(currentUser, post, reqBody.content());

        return new ApiResponse<>(
                HttpStatus.CREATED,
                "%d번 댓글이 생성되었습니다.".formatted(newComment.getId()),
                new CommentResponse(newComment)
        );
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 수정")
    public ApiResponse<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentCreateRequest reqBody
    ) {
        User currentUser = rq.getCurrentUser();  // rq 추가 필요

        // NOTE: commentService.updateComment(currentUser, commentId, newContent) 메서드가 필요하며,
        // 이 메서드 내에서 '해당 댓글 ID 존재 여부 확인', '현재 사용자가 댓글 작성자인지 확인(권한 체크)', '댓글 내용 업데이트' 로직을 처리해야 합니다.
        Comment updatedComment = commentService.updateComment(
                currentUser,
                commentId,
                reqBody.content()
        );

        return new ApiResponse<>(
                HttpStatus.OK,
                "%d번 댓글이 성공적으로 수정되었습니다.".formatted(updatedComment.getId()),
                new CommentResponse(updatedComment)
        );
    }

}
