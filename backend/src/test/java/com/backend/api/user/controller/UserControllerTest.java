package com.backend.api.user.controller;

import com.backend.api.user.dto.request.UserLoginRequest;
import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원가입 API")
    class t1{

        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            UserSignupRequest request = new UserSignupRequest(
                    "test@naver.com",
                    "test1234",
                    "test",
                    "testnick",
                    27,
                    "https://github.com/test",
                    null
            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            User user = userRepository.findByEmail(request.email()).orElseThrow();


            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("signup"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("%d번 회원가입이 완료되었습니다.".formatted(user.getId())))
                    .andExpect(jsonPath("$.data.id").value(user.getId()))
                    .andExpect(jsonPath("$.data.email").value(request.email()))
                    .andExpect(jsonPath("$.data.name").value(request.name()))
                    .andExpect(jsonPath("$.data.nickname").value(user.getNickname()))
                    .andExpect(jsonPath("$.data.age").value(user.getAge()))
                    .andExpect(jsonPath("$.data.github").value(user.getGithub()))
                    .andExpect(jsonPath("$.data.role").value(user.getRole().name()))
                    .andDo(print());;
        }

        @Test
        @DisplayName("이미 존재하는 이메일로 회원가입")
        void fail() throws Exception {

            User existingUser = userRepository.save(User.builder()
                    .email("test@naver.com")
                    .password(passwordEncoder.encode("test1234"))
                    .name("test")
                    .nickname("testnick")
                    .age(27)
                    .github("https://github.com/test")
                    .role(Role.USER)
                    .build());


            UserSignupRequest request = new UserSignupRequest(
                    "test@naver.com",
                    "test1234",
                    "test",
                    "testnick",
                    27,
                    "https://github.com/test",
                    null
            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            User user = userRepository.findByEmail(request.email()).orElseThrow();


            resultActions
                    .andExpect(handler().handlerType(UserController.class))
                    .andExpect(handler().methodName("signup"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("CONFLICT"))
                    .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                    .andDo(print());
        }

    }


    @Nested
    @DisplayName("로그인 API")
    class t2{

        @BeforeEach
        void setUp() {
            User user = User.builder()
                    .email("test@naver.com")
                    .age(27)
                    .github("https://github.com/test")
                    .name("test")
                    .password(passwordEncoder.encode("test1234"))
                    .image(null)
                    .role(Role.USER)
                    .nickname("testnick")
                    .build();
            userRepository.save(user);

        }

        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            UserLoginRequest request = new UserLoginRequest(
                    "test@naver.com",
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
                    .andExpect(jsonPath("$.message").value("%d번 회원의 로그인을 성공했습니다.".formatted(user.getId())))
                    .andExpect(jsonPath("$.data.id").value(user.getId()))
                    .andExpect(jsonPath("$.data.email").value(request.email()))
                    .andExpect(jsonPath("$.data.nickname").value(user.getNickname()))
                    .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(user.getCreateDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(user.getModifyDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.data.role").value(user.getRole().name()))
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
                    "test@naver.com",
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

}
