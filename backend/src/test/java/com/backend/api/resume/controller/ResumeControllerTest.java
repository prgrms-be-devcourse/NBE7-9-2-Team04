package com.backend.api.resume.controller;


import com.backend.api.resume.dto.request.ResumeCreateRequest;
import com.backend.api.resume.dto.request.ResumeUpdateRequest;
import com.backend.domain.resume.entity.Resume;
import com.backend.domain.resume.repository.ResumeRepository;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
    @Transactional
    void setUp() {
        User user = User.builder()
                .email("test@naver.com")
                .age(27)
                .github("https://github.com/test")
                .name("test")
                .password("test1234")
                .image(null)
                .role(User.Role.USER)
                .nickname("testnick")
                .build();

        User user2 = User.builder()
                .email("test2@naver.com")
                .age(20)
                .github("https://github.com/test")
                .name("test")
                .password("test1234")
                .image(null)
                .role(User.Role.USER)
                .nickname("testnick")
                .build();
        userRepository.save(user);
        userRepository.save(user2);

        Resume resume = Resume.builder()
                .content("이력서 내용입니다.")
                .skill("Java, Spring Boot")
                .activity("대외 활동 내용입니다.")
                .certification("없음")
                .career("경력 사항 내용입니다.")
                .portfolioUrl("http://portfolio.example.com")
                .user(user2)
                .build();

        resumeRepository.save(resume);

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
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("CREATED"))
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

    @Nested
    @DisplayName("이력서 수정 API")
    class t2 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            //given
            ResumeUpdateRequest request = new ResumeUpdateRequest(
                    "수정된 이력서 내용입니다.",
                    "Java, Spring Boot, mysql",
                    "수정된 대외 활동 내용입니다.",
                    "없음",
                    "수정된 경력 사항 내용입니다.",
                    "http://portfolio.example2.com"
            );

            Long userId = 2L;

            Long resumeId = 1L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/users/resumes/%d/%d" .formatted(userId, resumeId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("updateResume"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("이력서가 수정되었습니다."))
                    .andExpect(jsonPath("$.data.userId").value(2))
                    .andExpect(jsonPath("$.data.content").value("수정된 이력서 내용입니다."))
                    .andExpect(jsonPath("$.data.skill").value("Java, Spring Boot, mysql"))
                    .andExpect(jsonPath("$.data.activity").value("수정된 대외 활동 내용입니다."))
                    .andExpect(jsonPath("$.data.certification").value("없음"))
                    .andExpect(jsonPath("$.data.career").value("수정된 경력 사항 내용입니다."))
                    .andExpect(jsonPath("$.data.portfolioUrl").value("http://portfolio.example2.com"))
                    .andDo(print());
        }

        @Test
        @DisplayName("이력서가 존재하지 않을 때")
        void fail1() throws Exception {
            //given
            ResumeUpdateRequest request = new ResumeUpdateRequest(
                    "수정된 이력서 내용입니다.",
                    "Java, Spring Boot, mysql",
                    "수정된 대외 활동 내용입니다.",
                    "없음",
                    "수정된 경력 사항 내용입니다.",
                    "http://portfolio.example2.com"
            );
            Long userId = 1L;
            Long resumeId = 999L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/users/resumes/%d/%d" .formatted(userId, resumeId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("updateResume"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("이력서를 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("작성자 불일치")
        @Transactional
        void fail2() throws Exception {
            //given
            ResumeUpdateRequest request = new ResumeUpdateRequest(
                    "수정된 이력서 내용입니다.",
                    "Java, Spring Boot, mysql",
                    "수정된 대외 활동 내용입니다.",
                    "없음",
                    "수정된 경력 사항 내용입니다.",
                    "http://portfolio.example2.com"
            );
            Long userId = 1L;
            User user = userRepository.findById(userId).orElseThrow();
            Resume resume = Resume.builder()
                    .content("이력서 내용입니다.")
                    .skill("Java, Spring Boot")
                    .activity("대외 활동 내용입니다.")
                    .certification("없음")
                    .career("경력 사항 내용입니다.")
                    .portfolioUrl("http://portfolio.example.com")
                    .user(user)
                    .build();
            resumeRepository.save(resume);
            Long resumeId = 1L;

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/users/resumes/%d/%d" .formatted(userId, resumeId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("updateResume"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("이력서 수정 권한이 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("이력서 삭제 API")
    class t3 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            // given
            Long userId = 2L;
            Long resumeId = 1L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/users/resumes/%d/%d" .formatted(userId, resumeId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("deleteResume"))
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$.status").value("NO_CONTENT"))
                    .andExpect(jsonPath("$.message").value("이력서가 삭제되었습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("이력서가 존재하지 않을 때")
        void fail1() throws Exception {
            // given
            Long userId = 2L;
            Long resumeId = 999L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/users/resumes/%d/%d" .formatted(userId, resumeId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("deleteResume"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("이력서를 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("작성자 불일치")
        void fail2() throws Exception {
            // given
            Long userId = 1L;
            Long resumeId = 1L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/users/resumes/%d/%d" .formatted(userId, resumeId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("deleteResume"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("이력서 수정 권한이 없습니다."))
                    .andDo(print());
        }
        @Test
        @DisplayName("유저가 존재하지 않을 때")
        void fail4() throws Exception {
            // given
            Long userId = 999L;
            Long resumeId = 1L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/users/resumes/%d/%d" .formatted(userId, resumeId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("deleteResume"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("이력서 조회 API")
    class t4 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            // given
            Long userId = 2L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/resumes/%d" .formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("getResume"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("이력서를 조회했습니다."))
                    .andExpect(jsonPath("$.data.userId").value(2))
                    .andExpect(jsonPath("$.data.content").value("이력서 내용입니다."))
                    .andExpect(jsonPath("$.data.skill").value("Java, Spring Boot"))
                    .andExpect(jsonPath("$.data.activity").value("대외 활동 내용입니다."))
                    .andExpect(jsonPath("$.data.certification").value("없음"))
                    .andExpect(jsonPath("$.data.career").value("경력 사항 내용입니다."))
                    .andExpect(jsonPath("$.data.portfolioUrl").value("http://portfolio.example.com"))
                    .andDo(print());
        }

        @Test
        @DisplayName("이력서가 존재하지 않을 때")
        void fail1() throws Exception {
            // given
            Long userId = 1L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/resumes/%d" .formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("getResume"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("이력서를 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("유저가 존재하지 않을 때")
        void fail2() throws Exception {
            // given
            Long userId = 999L;
            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/resumes/%d" .formatted(userId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );
            // then
            resultActions
                    .andExpect(handler().handlerType(ResumeController.class))
                    .andExpect(handler().methodName("getResume"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다."))
                    .andDo(print());
        }

    }

}