package com.backend.api.comment.controller;

import com.backend.api.comment.dto.request.CommentCreateRequest;
import com.backend.api.comment.dto.response.CommentResponse;
import com.backend.api.comment.service.CommentService;
import com.backend.api.post.service.PostService;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.post.entity.Post;
import com.backend.domain.user.entity.User;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "CommentController", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final Rq rq;

    @PostMapping("/{postId}/comments")
    @Operation(summary = "댓글 작성")
    public ApiResponse<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequest reqBody
    ) {

        User currentUser = rq.getUser();

        Comment newComment = commentService.writeComment(currentUser, postId, reqBody.content());

        return ApiResponse.created(
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

        User currentUser = rq.getUser();

        Comment updatedComment = commentService.updateComment(
                currentUser,
                commentId,
                reqBody.content()
        );

        return ApiResponse.ok(
                "%d번 댓글이 수정되었습니다.".formatted(updatedComment.getId()),
                new CommentResponse(updatedComment)
        );
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId
    ) {

        User currentUser = rq.getUser();

        commentService.deleteComment(currentUser, commentId);

        return ApiResponse.ok(
                "%d번 댓글이 삭제되었습니다.".formatted(commentId),
                null
        );
    }

    @GetMapping("/{postId}/comments")
    @Operation(summary = "댓글 목록 조회")
    public ApiResponse<List<CommentResponse>> getComments(
            @PathVariable Long postId
    ) {

        Post post = postService.findPostByIdOrThrow(postId);

        List<CommentResponse> commentResponseList = post.getComments().reversed().stream()
                .map(CommentResponse::new)
                .toList();

        return ApiResponse.ok(
                "%d번 게시글의 댓글 목록 조회 성공".formatted(postId),
                commentResponseList
        );
    }

}
