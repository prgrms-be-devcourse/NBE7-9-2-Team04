package com.backend.api.user.controller;

import com.backend.api.user.dto.response.UserMyPageResponse;
import com.backend.api.user.service.UserMyPageService;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name ="Users", description = "마이페이지 관련 API")
public class UserMyPageController {

    private final UserMyPageService userMyPageService;
    private final Rq rq;

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
        return ApiResponse.ok("개인 정보 수정이 완료되었습니다.",response);
    }


}
