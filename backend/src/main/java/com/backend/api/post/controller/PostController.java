package com.backend.api.post.controller;


import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.post.service.PostService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {


    private final PostService postService;

    // private final Long TEMP_USER_ID = 1L;

    @PostMapping
    @Operation(summary = "게시글 생성")
    public ApiResponse<PostResponse> createPost(
            @Valid @RequestBody PostAddRequest request,
            @AuthenticationPrincipal Long userId) {

        PostResponse response = postService.createPost(request, userId);

        return ApiResponse.ok(
                "%d번 게시글 등록을 완료했습니다.".formatted(response.postId()),
                response
        );
    }


    @GetMapping("/{postId}")
    @Operation(summary = "특정 게시글 조회")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {

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
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal Long userId) {

        PostResponse response = postService.updatePost(postId, request, userId);

        return ApiResponse.ok(
                "%d번 게시글 수정을 완료했습니다.".formatted(postId),
                response
        );
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId) {

        postService.deletePost(postId, userId);

        return ApiResponse.ok("게시글 삭제가 완료되었습니다.", null);
    }
}
