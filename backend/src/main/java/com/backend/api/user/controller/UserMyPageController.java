package com.backend.api.user.controller;

import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.answer.dto.response.AnswerReadResponse;
import com.backend.api.answer.service.AnswerService;
import com.backend.api.comment.dto.response.CommentResponse;
import com.backend.api.comment.service.CommentService;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.post.service.PostService;
import com.backend.api.post.service.PostService;
import com.backend.api.user.dto.request.UserMyPageQuestionRequest;
import com.backend.api.user.dto.response.UserMyPageResponse;
import com.backend.api.user.service.UserMyPageService;
import com.backend.api.user.service.UserService;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name = "Users", description = "마이페이지 관련 API")
public class UserMyPageController {

    private final UserService userService;
    private final PostService postService;
    private final UserMyPageService userMyPageService;
    private final Rq rq;
    private UserMyPageQuestionRequest userMyPageQuestionRequest;
    private final CommentService commentService;
    private final AnswerService answerService;

    @GetMapping("/me")
    @Operation(summary = "개인 정보 조회")
    public ApiResponse<UserMyPageResponse> detailInformation() {
        Long userId = rq.getUser().getId();
        return ApiResponse.ok(userMyPageService.getInformation(userId));
    }

    @PutMapping("/me")
    @Operation(summary = "개인 정보 수정")
    public ApiResponse<UserMyPageResponse> updateUser(
            @RequestBody UserMyPageResponse.UserModify modify) {

        Long userId = rq.getUser().getId();
        UserMyPageResponse response = userMyPageService.modifyUser(userId, modify);
        return ApiResponse.ok("개인 정보 수정이 완료되었습니다.", response);

    }

    @GetMapping("/{userId}")
    @Operation(summary = "마이페이지 상세 정보 조회")
    public ApiResponse<UserMyPageResponse> detailInformation(@PathVariable Long userId) {
        UserMyPageResponse response = userMyPageService.getInformation(userId);
        return ApiResponse.ok("사용자 상세 정보 조회를 완료했습니다.", response);
    }

    @GetMapping("/{userId}/posts")
    @Operation(summary = "사용자가 작성한 모집글 목록 조회")
    public ApiResponse<List<PostResponse>> getUserPosts(@PathVariable Long userId) {
        List<PostResponse> userPosts = postService.getPostsByUserId(userId);
        return ApiResponse.ok(
                "사용자가 작성한 모집글 목록 조회를 완료했습니다.",
                userPosts
        );
    }

    @GetMapping
    @Operation(summary = "해결한 문제")
    public ApiResponse<List<UserMyPageResponse.SolvedProblem>> solvedProblemList() {
        Long userId = rq.getUser().getId();
        List<UserMyPageResponse.SolvedProblem> solvedList = userMyPageService.getSolvedProblems(userId);
        return ApiResponse.ok(solvedList);
    }

    @GetMapping("/{userId}/comments")
    @Operation(summary = "사용자가 작성한 댓글 목록 조회")
    public ApiResponse<List<CommentResponse>> getUserComments(
            @PathVariable Long userId
    ) {
        List<CommentResponse> userComments = commentService.getCommentsByUserId(userId);
        return ApiResponse.ok(
                "사용자가 작성한 댓글 목록 조회를 완료했습니다.",
                userComments
        );
    }

    @GetMapping("/{userId}/answers")
    @Operation(summary = "사용자가 작성한 면접 답변 목록 조회")
    public ApiResponse<List<AnswerReadResponse>> getUserAnswers(
            @PathVariable Long userId
    ) {
        List<AnswerReadResponse> userAnswers = answerService.findAnswersByUserId(userId);
        return ApiResponse.ok(
                "사용자가 작성한 면접 답변 목록 조회를 완료했습니다.",
                userAnswers
        );
    }
}
