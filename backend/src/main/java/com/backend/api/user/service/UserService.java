package com.backend.api.user.service;

import com.backend.api.user.dto.request.UserLoginRequest;
import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.api.user.dto.response.TokenResponse;
import com.backend.domain.user.entity.AccountStatus;
import com.backend.domain.user.entity.Role;
import com.backend.api.user.dto.response.UserMyPageResponse;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import com.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User signUp(UserSignupRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ErrorException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .nickname(request.nickname())
                .age(request.age())
                .github(request.github())
                .image(request.image())
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User login(UserLoginRequest request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_EMAIL));

        if(!user.validateLoginAvaliable()) {
            if(user.getAccountStatus() == AccountStatus.BANNED) {
                throw new ErrorException(ErrorCode.ACCOUNT_BANNED);
            }
            if(user.getAccountStatus() == AccountStatus.DEACTIVATED) {
                throw new ErrorException(ErrorCode.ACCOUNT_DEACTIVATED);
            }
        }

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new ErrorException(ErrorCode.WRONG_PASSWORD);
        }

        return user;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));
    }

    public TokenResponse createAccessTokenFromRefresh(String refreshToken) {

        //refreshToken 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ErrorException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        //refreshToken으로부터 id 추출
        Long userId = jwtTokenProvider.getIdFromToken(refreshToken);
        if (userId == null) {
            throw new ErrorException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));


        //새로운 토큰 발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

}
