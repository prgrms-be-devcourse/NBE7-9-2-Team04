package com.backend.api.user.controller;

import com.backend.api.user.dto.response.UserMyPageResponse;
import com.backend.api.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name ="Users", description = "마이페이지 관련 API")
public class UserMyPageController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserMyPageResponse> detailInformation(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getInformation(userId));
    }
}
