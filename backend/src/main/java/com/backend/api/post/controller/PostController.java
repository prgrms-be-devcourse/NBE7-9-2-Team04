package com.backend.api.post.controller;

import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.post.service.PostService;
import com.backend.domain.user.entity.User;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {
    private final PostService postService;
    private final Rq rq; // Rq 의존성 주입

    @PostMapping
    @Operation(summary = "게시글 생성", description = "유저가 게시물을 등록합니다.")
    public ApiResponse<PostResponse> createPost(
            @Valid @RequestBody PostAddRequest request) { // @AuthenticationPrincipal 제거

        User user = rq.getUser(); // Rq를 통해 User 객체 가져오기
        PostResponse response = postService.createPost(request, user); // User 객체를 서비스로 전달

        return ApiResponse.ok(
                "%d번 게시글 등록을 완료했습니다.".formatted(response.postId()),
                response
        );
    }


    @GetMapping("/{postId}")
    @Operation(summary = "특정 게시글 조회")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        // 이 메서드는 인증이 필요 없으므로 변경사항 없음
        PostResponse response = postService.getPost(postId);

        return ApiResponse.ok(
                "%d번 게시글을 성공적으로 조회했습니다.".formatted(postId),
                response
        );
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request) { // @AuthenticationPrincipal 제거

        User user = rq.getUser(); // Rq를 통해 User 객체 가져오기
        PostResponse response = postService.updatePost(postId, request, user); // User 객체를 서비스로 전달

        return ApiResponse.ok(
                "%d번 게시글 수정을 완료했습니다.".formatted(postId),
                response
        );
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) { // @AuthenticationPrincipal 제거

        User user = rq.getUser(); // Rq를 통해 User 객체 가져오기
        postService.deletePost(postId, user); // User 객체를 서비스로 전달

        return ApiResponse.ok("게시글 삭제가 완료되었습니다.", null);
    }
}
