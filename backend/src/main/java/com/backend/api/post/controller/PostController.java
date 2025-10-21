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


import java.util.Collections;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @PostMapping
    @Operation(summary = "게시글 생성", description = "유저가 게시물을 등록합니다.")
    public ApiResponse<PostResponse> createPost(
            @Valid @RequestBody PostAddRequest request) {

        User user = getCurrentUser();
        PostResponse response = postService.createPost(request, user);

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

    @GetMapping("/pinned")
    @Operation(summary = "상단 고정 게시글 목록 조회")
    public ApiResponse<List<PostResponse>> getPinnedPosts() {
        List<PostResponse> postResponseList = postService.getPinnedPosts();

        return ApiResponse.ok(
                "상단 고정된 게시글을 성공적으로 조회했습니다.",
                postResponseList
        );
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request) {

        User user = getCurrentUser();
        PostResponse response = postService.updatePost(postId, request, user);

        return ApiResponse.ok(
                "%d번 게시글 수정을 완료했습니다.".formatted(postId),
                response
        );
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {

        User user = getCurrentUser();
        postService.deletePost(postId, user);

        return ApiResponse.ok("게시글 삭제가 완료되었습니다.", null);
    }

    private User getCurrentUser() {
        return rq.getUser();
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 단건 조회")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse response = postService.getPost(postId);

        return ApiResponse.ok(
                "%d번 게시글을 성공적으로 조회했습니다.".formatted(postId),
                response
        );
    }

    @GetMapping
    @Operation(summary = "게시글 다건 조회")
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ApiResponse.ok(
                "전체 게시글 조회 성공", posts);
    }
}
