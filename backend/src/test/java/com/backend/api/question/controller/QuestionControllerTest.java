package com.backend.api.question.controller;

import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.api.question.dto.request.QuestionUpdateRequest;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository questionRepository;

    private Question savedQuestion;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAll();

        Question question = Question.builder()
                .title("기존 제목")
                .content("기존 내용")
                .build();

        savedQuestion = questionRepository.save(question);
    }

    @Nested
    @DisplayName("질문 생성 API")
    class t1 {

        @Test
        @DisplayName("질문 생성 성공")
        void success() throws Exception {
            QuestionAddRequest request = new QuestionAddRequest(
                    "Spring Boot란?",
                    "Spring Boot의 핵심 개념과 장점을 설명해주세요.",
                    null // 카테고리 미구현으로 null 처리
            );

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문이 생성되었습니다."))
                    .andExpect(jsonPath("$.data.title").value("Spring Boot란?"))
                    .andExpect(jsonPath("$.data.content").value("Spring Boot의 핵심 개념과 장점을 설명해주세요."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 생성 실패 - 제목 누락")
        void fail1() throws Exception {
            QuestionAddRequest request = new QuestionAddRequest(
                    "", // 제목 누락
                    "내용입니다.",
                    null
            );

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("질문 제목은 필수입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 생성 실패 - 내용 누락")
        void fail2() throws Exception {
            QuestionAddRequest request = new QuestionAddRequest(
                    "Spring Boot란?",
                    "",
                    null
            );

            mockMvc.perform(post("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("질문 내용은 필수입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("질문 수정 API")
    class t2 {

        @Test
        @DisplayName("질문 수정 성공")
        void success() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionUpdateRequest request = new QuestionUpdateRequest(
                    "수정된 제목",
                    "수정된 내용",
                    null // 카테고리 미구현으로 null 처리
            );

            mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문이 수정되었습니다."))
                    .andExpect(jsonPath("$.data.title").value("수정된 제목"))
                    .andExpect(jsonPath("$.data.content").value("수정된 내용"))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 수정 실패 - 존재하지 않는 ID")
        void fail1() throws Exception {
            Long questionId = 999L;
            QuestionUpdateRequest request = new QuestionUpdateRequest(
                    "수정된 제목",
                    "수정된 내용",
                    null // 카테고리 미구현으로 null 처리
            );

            mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 수정 실패 - 제목 누락")
        void fail2() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionUpdateRequest request = new QuestionUpdateRequest(
                    "",
                    "내용만 있습니다.",
                    null // 카테고리 미구현으로 null 처리
            );

            mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("질문 제목은 필수입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 수정 실패 - 내용 누락")
        void fail3() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionUpdateRequest request = new QuestionUpdateRequest(
                    "제목만 있습니다.",
                    "",
                    null // 카테고리 미구현으로 null 처리
            );

            mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("질문 내용은 필수입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("질문 조회 API")
    class t3 {

        @Test
        @DisplayName("질문 목록 조회 성공 - 승인된 질문만 반환")
        void success() throws Exception {
            // given
            Question approvedQuestion = questionRepository.save(
                    Question.builder()
                            .title("승인된 질문")
                            .content("승인된 질문 내용")
                            .build()
            );
            approvedQuestion.setApproved(true);

            Question unapprovedQuestion = questionRepository.save(
                    Question.builder()
                            .title("미승인 질문")
                            .content("미승인 질문 내용")
                            .build()
            );
            unapprovedQuestion.setApproved(false);

            // when & then
            mockMvc.perform(get("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문 목록 조회 성공"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].isApproved").value(true))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 목록 조회 실패 - 데이터 없음")
        void fail1() throws Exception {
            // when & then
            mockMvc.perform(get("/api/v1/questions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("질문 단건 조회 API")
    class t4 {

        @Test
        @DisplayName("질문 단건 조회 성공")
        void success() throws Exception {
            Question question = questionRepository.save(
                    Question.builder()
                            .title("상세 질문")
                            .content("상세 질문 내용")
                            .build()
            );
            question.setApproved(true);

            mockMvc.perform(get("/api/v1/questions/{questionId}", question.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문 단건 조회 성공"))
                    .andExpect(jsonPath("$.data.questionId").value(question.getId()))
                    .andExpect(jsonPath("$.data.title").value("상세 질문"))
                    .andExpect(jsonPath("$.data.content").value("상세 질문 내용"))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 상세 조회 실패 - 존재하지 않는 ID")
        void fail1() throws Exception {
            Long invalidId = 999L;

            mockMvc.perform(get("/api/v1/questions/{questionId}", invalidId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 상세 조회 실패 - 승인되지 않은 질문 접근")
        void fail2() throws Exception {
            Question question = questionRepository.save(
                    Question.builder()
                            .title("미승인 질문")
                            .content("미승인 질문 내용")
                            .build()
            );
            question.setApproved(false);

            mockMvc.perform(get("/api/v1/questions/{questionId}", question.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("승인되지 않은 질문입니다."))
                    .andDo(print());
        }
    }
}
