package com.backend.api.user.controller;

import com.backend.api.user.dto.request.UserLoginRequest;
import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.api.user.dto.response.TokenResponse;
import com.backend.api.user.dto.response.UserResponse;
import com.backend.api.user.service.UserService;
import com.backend.domain.user.entity.User;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import com.backend.global.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name ="Users", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Rq rq;

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인")
    public ApiResponse<UserResponse> login(@RequestBody UserLoginRequest request){

        User user = userService.login(request);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        rq.setCookie("accessToken", accessToken, (int) (jwtTokenProvider.getAccessTokenExpireTime()));
        rq.setCookie("refreshToken", refreshToken, (int) (jwtTokenProvider.getRefreshTokenExpireTime()));

        return ApiResponse.ok(
                "%d번 회원의 로그인을 성공했습니다.".formatted(user.getId()),
                UserResponse.from(user));
    }

    @DeleteMapping("/logout")
    @Operation(summary = "사용자 로그아웃")
    public ApiResponse<Void> logout(){

        rq.deleteCookie("accessToken");
        rq.deleteCookie("refreshToken");
        return ApiResponse.ok("로그아웃이 되었습니다.", null);
    }

    @PostMapping("/signup")
    @Operation(summary = "사용자 회원가입")
    public ApiResponse<UserResponse> signup(@RequestBody UserSignupRequest request){
        User user = userService.signUp(request);
        return ApiResponse.ok(
                "%d번 회원가입이 완료되었습니다.".formatted(user.getId()),
                UserResponse.from(user)
        );
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    public ApiResponse<Void> refresh(){

        String refreshToken = rq.getCookieValue("refreshToken");

        if(refreshToken == null){
            throw new ErrorException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        TokenResponse newTokens = userService.createAccessTokenFromRefresh(refreshToken);

        rq.setCookie("accessToken", newTokens.accessToken(), (int) (jwtTokenProvider.getAccessTokenExpireTime()));
        rq.setCookie("refreshToken", newTokens.refreshToken(), (int) (jwtTokenProvider.getRefreshTokenExpireTime()));

        return ApiResponse.ok("새로운 토큰이 발급되었습니다.",  null);
    }

    //

}