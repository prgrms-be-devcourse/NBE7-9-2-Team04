package com.backend.api.resume.controller;


import com.backend.api.resume.dto.request.ResumeCreateRequest;
import com.backend.domain.resume.repository.ResumeRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUp() {
        User user = User.builder()
                .email("test@naver.com")
                .age(27)
                .github("https://github.com/test")
                .name("test")
                .password("test1234")
                .image(null)
                .role(Role.USER)
                .nickname("testnick")
                .build();
        userRepository.save(user);

    }


    @Nested
    @DisplayName("이력서 생성 API")
    class t1 {

        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            // given
            ResumeCreateRequest request = new ResumeCreateRequest(
                    "이력서 내용입니다.",
                    "Java, Spring Boot",
                    "대외 활동 내용입니다.",
                    "없음",
                    "경력 사항 내용입니다.",
                    "http://portfolio.example.com"
            );
            Long userId = 1L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/resumes/%d".formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("createResume"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("이력서가 생성되었습니다."))
                    .andExpect(jsonPath("$.data.userId").value(1))
                    .andExpect(jsonPath("$.data.content").value("이력서 내용입니다."))
                    .andExpect(jsonPath("$.data.skill").value("Java, Spring Boot"))
                    .andExpect(jsonPath("$.data.activity").value("대외 활동 내용입니다."))
                    .andExpect(jsonPath("$.data.certification").value("없음"))
                    .andExpect(jsonPath("$.data.career").value("경력 사항 내용입니다."))
                    .andExpect(jsonPath("$.data.portfolioUrl").value("http://portfolio.example.com"))
                    .andDo(print());
        }

        @Test
        @DisplayName("userId가 존재하지 않을 떄")
        void fail1() throws Exception {
            // given
            ResumeCreateRequest request = new ResumeCreateRequest(
                    "이력서 내용입니다.",
                    "Java, Spring Boot",
                    "대외 활동 내용입니다.",
                    "없음",
                    "경력 사항 내용입니다.",
                    "http://portfolio.example.com"
            );
            Long userId = 999L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/resumes/%d".formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("createResume"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("이미 이력서가 존재할 때")
        void fail2() throws Exception {
            // given
            ResumeCreateRequest request = new ResumeCreateRequest(
                    "이력서 내용입니다.",
                    "Java, Spring Boot",
                    "대외 활동 내용입니다.",
                    "없음",
                    "경력 사항 내용입니다.",
                    "http://portfolio.example.com"
            );
            Long userId = 1L;
            // when
            mockMvc.perform(
                    post("/api/v1/users/resumes/%d".formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/users/resumes/%d".formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("createResume"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("이미 등록된 이력서가 있습니다."))
                    .andDo(print());
        }
    }

}