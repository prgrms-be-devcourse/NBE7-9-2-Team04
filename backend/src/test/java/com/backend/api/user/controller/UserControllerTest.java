package com.backend.api.user.controller;

import com.backend.api.user.dto.request.UserLoginRequest;
import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.entity.VerificationCode;
import com.backend.domain.user.repository.UserRepository;
import com.backend.domain.user.repository.VerificationCodeRepository;
import com.backend.global.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Nested
    @DisplayName("회원가입 API")
    class t1 {

        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            // given
            String email = "signup1@naver.com";

            //이메일 인증 완료 상태를 DB에 저장
            verificationCodeRepository.save(
                    VerificationCode.builder()
                            .email(email)
                            .code("ABC123")
                            .verified(true)
                            .expiresAt(LocalDateTime.now().plusMinutes(5))
                            .build()
            );

            UserSignupRequest request = new UserSignupRequest(
                    email,
                    "test1234",
                    "test",
                    "signupNick",
                    27,
                    "https://github.com/signup",
                    null
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            User user = userRepository.findByEmail(request.email()).orElseThrow();

            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("signup"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
                    .andExpect(jsonPath("$.data.email").value(request.email()))
                    .andExpect(jsonPath("$.data.nickname").value(request.nickname()))
                    .andDo(print());
        }

        @Test
        @DisplayName("이미 존재하는 이메일로 회원가입")
        void fail() throws Exception {
            User existingUser = userRepository.save(User.builder()
                    .email("signup2@naver.com")
                    .password(passwordEncoder.encode("test1234"))
                    .name("test")
                    .nickname("signupNick2")
                    .age(27)
                    .github("https://github.com/signup2")
                    .role(Role.USER)
                    .build());

            UserSignupRequest request = new UserSignupRequest(
                    "signup2@naver.com",
                    "test1234",
                    "test",
                    "signupNick2",
                    27,
                    "https://github.com/signup2",
                    null
            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("signup"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("CONFLICT"))
                    .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("이메일 인증 미완료 상태에서 회원가입 실패")
        void fail_notVerifiedEmail() throws Exception {
            String email = "signup3@naver.com";

            //인증코드는 있지만 verified=false 인 상태
            verificationCodeRepository.save(
                    VerificationCode.builder()
                            .email(email)
                            .code("XYZ999")
                            .verified(false)
                            .expiresAt(LocalDateTime.now().plusMinutes(5))
                            .build()
            );

            UserSignupRequest request = new UserSignupRequest(
                    email,
                    "test1234",
                    "test",
                    "signupNick3",
                    27,
                    "https://github.com/signup3",
                    null
            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("이메일 인증이 완료되지 않았습니다."))
                    .andDo(print());
        }
    }


    @Nested
    @DisplayName("로그인 API")
    class t2{

        @BeforeEach
        void setUp() {
            User user = User.builder()
                    .email("login1@naver.com")
                    .age(27)
                    .github("https://github.com/login1")
                    .name("test")
                    .password(passwordEncoder.encode("test1234"))
                    .image(null)
                    .role(Role.USER)
                    .nickname("loginNick")
                    .build();
            userRepository.save(user);

        }

        @Test
        @DisplayName("정상 작동 - 토큰 설정 확인")
        void success() throws Exception {
            UserLoginRequest request = new UserLoginRequest(
                    "login1@naver.com",
                    "test1234"

            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            User user = userRepository.findByEmail(request.email()).orElseThrow();


            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("login"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("로그인을 성공했습니다."))
                    .andExpect(jsonPath("$.data.id").value(user.getId()))
                    .andExpect(jsonPath("$.data.email").value(request.email()))
                    .andExpect(jsonPath("$.data.nickname").value(user.getNickname()))
                    .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(user.getCreateDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(user.getModifyDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.data.role").value(user.getRole().name()))

                    //Access Token 쿠키 검증
                    .andExpect(cookie().exists("accessToken"))
                    .andExpect(cookie().httpOnly("accessToken", true))
                    .andExpect(cookie().secure("accessToken", true))
                    .andExpect(cookie().path("accessToken", "/"))
                    .andExpect(cookie().value("accessToken", Matchers.not(Matchers.emptyString())))

                    //Refresh Token 쿠키 검증
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().httpOnly("refreshToken", true))
                    .andExpect(cookie().secure("refreshToken", true))
                    .andExpect(cookie().path("refreshToken", "/"))
                    .andExpect(cookie().value("refreshToken", Matchers.not(Matchers.emptyString())))
                    .andDo(print());;
        }

        @Test
        @DisplayName("이메일 불일치")
        void fail1() throws Exception {
            UserLoginRequest request = new UserLoginRequest(
                    "wrong@naver.com",
                    "test1234"
            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("login"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("이메일이 존재하지 않습니다."))
                    .andDo(print());;
        }

        @Test
        @DisplayName("비밀번호 불일치")
        void fail2() throws Exception {
            UserLoginRequest request = new UserLoginRequest(
                    "login1@naver.com",
                    "wrong1234"
            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            User user = userRepository.findByEmail(request.email()).orElseThrow();


            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("login"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."))
                    .andDo(print());;
        }

    }



    @Test
    @DisplayName("로그아웃")
    void t3() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        delete("/api/v1/users/logout")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("로그아웃이 되었습니다."))
                .andDo(print());
    }


    @Nested
    @DisplayName("토큰 재발급 API")
    class t4{

        private String validRefreshToken;
        private User user;
        private Long userId;

        @BeforeEach
        void setUp() {
            user = User.builder()
                    .email("refresh@naver.com")
                    .age(27)
                    .github("https://github.com/refresh")
                    .name("test")
                    .password(passwordEncoder.encode("refresh1234"))
                    .image(null)
                    .role(Role.USER)
                    .nickname("refreshNick")
                    .build();

            userRepository.save(user);

            userId = user.getId();
            validRefreshToken = jwtTokenProvider.generateRefreshToken(
                    userId,
                    user.getEmail(),
                    user.getRole()
            );
        }

        @Test
        @DisplayName("토큰 갱신 정상 작동")
        void success() throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(
                            post("/api/v1/users/refresh")
                                    .cookie(new Cookie("refreshToken", validRefreshToken))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("refresh"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("새로운 토큰이 발급되었습니다."))
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().exists("accessToken"))
                    .andDo(print());

        }

        @Test
        @DisplayName("존재하지 않는 refreshToken")
        void fail1() throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(
                            //토큰 없이 요청
                            post("/api/v1/users/refresh")
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("refresh"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.message").value("Refresh Token을 찾을 수 없습니다."))
                    .andDo(print());
        }


        @Test
        @DisplayName("유효하지 않은 refreshToken")
        void fail2() throws Exception {

            String wrongRefreshToken = "wrong-access-token";
            ResultActions resultActions = mockMvc
                    .perform(
                            post("/api/v1/users/refresh")

                                    .cookie(new Cookie("refreshToken", wrongRefreshToken))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("refresh"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.message").value("유효하지 않은 Refresh Token입니다."))
                    .andDo(print());
        }

    }

}
