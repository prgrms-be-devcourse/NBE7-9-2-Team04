package com.backend.api.user.service;

import com.backend.api.user.dto.request.UserLoginRequest;
import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.api.user.dto.response.TokenResponse;
import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.entity.SubscriptionType;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.domain.user.entity.AccountStatus;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import com.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SubscriptionRepository subscriptionRepository;

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

        userRepository.save(user);

        Subscription basicSubscription = Subscription.builder()
                .user(user)
                .subscriptionType(SubscriptionType.BASIC)
                .subscriptionName("BASIC")
                .isActive(true)                     // 기본 회원은 활성화된 상태로 시작
                .price(0L)                          // 무료
                .questionLimit(5)                   // 무료 사용자는 질문 제한 5회
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())        // BASIC은 실질적 만료 개념 X
                .nextBillingDate(LocalDate.now())    // 유료 전환 시점 기준으로 계산됨
                .customerKey(UUID.randomUUID().toString()) // Toss에서 사용할 유저별 key
                .billingKey(null)                    // 아직 유료결제X → null
                .build();

        subscriptionRepository.save(basicSubscription);

        return user;
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
