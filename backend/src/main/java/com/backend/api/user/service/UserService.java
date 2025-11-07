package com.backend.api.user.service;

import com.backend.api.user.dto.request.UserLoginRequest;
import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.api.user.dto.response.TokenResponse;
import com.backend.api.user.dto.response.UserLoginResponse;
import com.backend.api.user.dto.response.UserSignupResponse;
import com.backend.domain.ranking.entity.Ranking;
import com.backend.domain.ranking.entity.Tier;
import com.backend.domain.ranking.repository.RankingRepository;
import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.entity.SubscriptionType;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.domain.user.entity.AccountStatus;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.domain.user.repository.VerificationCodeRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import com.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final RankingRepository rankingRepository;
    private final RefreshRedisService refreshRedisService;

    @Transactional
    public UserSignupResponse signUp(UserSignupRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ErrorException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (!emailService.isVerified(request.email())) {
            throw new ErrorException(ErrorCode.EMAIL_NOT_VERIFIED);
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


              
      verificationCodeRepository.findByEmail(request.email())
        .ifPresent(verificationCodeRepository::delete);
      
        userRepository.save(user);

        Subscription basicSubscription = Subscription.builder()
                .user(user)
                .subscriptionType(SubscriptionType.BASIC)
                .subscriptionName("BASIC")
                .isActive(false)
                .price(0L)
                .questionLimit(5)                   // 무료 사용자는 질문 제한 5회
                .startDate(LocalDateTime.now())
                .endDate(null)        // BASIC은 실질적 만료 개념 X
                .nextBillingDate(null)
                .customerKey(UUID.randomUUID().toString()) // Toss에서 사용할 유저별 key
                .billingKey(null)                    // 아직 유료결제X → null
                .build();

        subscriptionRepository.save(basicSubscription);


        Ranking ranking = Ranking.builder()
                .user(user)
                .totalScore(0)
                .tier(Tier.UNRATED)
                .rankValue(0)
                .build();

        user.assignSubscription(basicSubscription);
        user.assignRanking(ranking);

        rankingRepository.save(ranking);

        return UserSignupResponse.from(user,ranking);
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest request){
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

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        refreshRedisService.saveRefreshToken(
                user.getId(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpireTime()
        );

        return UserLoginResponse.from(user,accessToken,refreshToken);
    }

    @Transactional
    public void logout(Long userId){
        refreshRedisService.deleteRefreshToken(userId);
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public TokenResponse createAccessTokenFromRefresh(String requestRefreshToken) {

        //클라이언트 요청으로부터 refreshToken 유효성 검사
        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new ErrorException(ErrorCode.INVALID_REFRESH_TOKEN);
        }


        //요청된 refreshToken으로부터 id 추출
        Long userId = jwtTokenProvider.getIdFromToken(requestRefreshToken);
        if (userId == null) {
            throw new ErrorException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        //redis에 저장된 refreshToken 조회
        String savedRefreshToken = refreshRedisService.getRefreshToken(userId);
        if(savedRefreshToken == null) {
            throw new ErrorException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        //요청과 redis 동일한지 비교
        if(!savedRefreshToken.equals(requestRefreshToken)) {
            throw  new ErrorException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));


        //새로운 토큰 발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        refreshRedisService.saveRefreshToken(
                user.getId(),
                newRefreshToken,
                jwtTokenProvider.getRefreshTokenExpireTime()
        );

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

}
