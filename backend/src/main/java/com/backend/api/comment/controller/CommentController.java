package com.backend.api.comment.controller;

import com.backend.api.comment.dto.request.CommentCreateRequest;
import com.backend.api.comment.dto.response.CommentResponse;
import com.backend.api.comment.service.CommentService;
import com.backend.api.post.service.PostService;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.post.entity.Post;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.global.dto.response.ApiResponse;
import com.backend.global.entity.BaseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "CommentController", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    private User createTemporaryUser() {
        User user = User.builder()
                .email("temp_user1@example.com")
                .password("hashed_temp_pw")
                .name("임시 사용자")
                .nickname("user1")
                .age(30)
                .github("https://github.com/temp1")
                .role(Role.USER)
                .build();
        try {
            // BaseEntity 클래스에서 protected id 필드에 접근
            java.lang.reflect.Field idField = BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true); // private/protected 필드에 접근 가능하게 설정
            idField.set(user, 1L);      // id 필드에 1L 값 강제 주입
            idField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("임시 사용자 ID 설정 중 오류 발생: " + e.getMessage());
            // 실제 애플리케이션에서는 ErrorException을 던지거나 로그를 남겨야 합니다.
        }

        return user;
    }

    @PostMapping("/{postId}/comments")
    @Operation(summary = "댓글 작성")
    public ApiResponse<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequest reqBody
    ) {

        // rq 추가 전 임시 로직
        User currentUser = createTemporaryUser();

        // User currentUser = rq.getCurrentUser(); // rq 추가 후에 사용할 로직

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

        // rq 추가 전 임시 로직
        User currentUser = createTemporaryUser();

        // User currentUser = rq.getCurrentUser(); // rq 추가 후에 사용할 로직

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
        // rq 추가 전 임시 로직
        User currentUser = createTemporaryUser();

        // User currentUser = rq.getCurrentUser(); // rq 추가 후에 사용할 로직

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

        Post post = postService.findById(postId).get();

        List<CommentResponse> commentResponseList = post.getComments().reversed().stream()
                .map(CommentResponse::new)
                .toList();

        return ApiResponse.ok(
                "%d번 게시글의 댓글 목록 조회 성공".formatted(postId),
                commentResponseList
        );
    }

}
