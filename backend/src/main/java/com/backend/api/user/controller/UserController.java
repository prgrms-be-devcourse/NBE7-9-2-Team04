package com.backend.api.user.controller;

import com.backend.api.user.dto.request.UserLoginRequest;
import com.backend.api.user.dto.response.UserResponse;
import com.backend.api.user.service.UserService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name ="Users", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;


                                           )

}
