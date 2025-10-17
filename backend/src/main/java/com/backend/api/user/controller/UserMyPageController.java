package com.backend.api.user.controller;

import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.user.dto.response.UserMyPageResponse;
import com.backend.api.user.service.UserMyPageService;
import com.backend.api.user.service.UserService;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.dto.response.ApiResponse;
import com.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name ="Users", description = "마이페이지 관련 API")
public class UserMyPageController {

    private final UserMyPageService userMyPageService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserMyPageResponse> detailInformation(@PathVariable Long userId) {
        return ResponseEntity.ok(userMyPageService.getInformation(userId));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "개인 정보 수정")
    public ResponseEntity<UserMyPageResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UserMyPageResponse.UserModify modify,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {

        if (!userDetails.getId().equals(userId)) {
            throw new AccessDeniedException("본인 정보만 수정할 수 있습니다.");
        }

        UserMyPageResponse response = userMyPageService.modifyUser(userId, modify);
        return ResponseEntity.ok(response);
    }
}
