package com.backend.api.question.controller;

import com.backend.api.question.dto.request.AdminQuestionAddRequest;
import com.backend.api.question.dto.request.QuestionApproveRequest;
import com.backend.api.question.dto.request.QuestionScoreRequest;
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
public class AdminQuestionControllerTest {

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
    @DisplayName("관리자용 질문 생성 API")
    class t1 {

        @Test
        @DisplayName("질문 생성 성공")
        void success() throws Exception {
            AdminQuestionAddRequest request = new AdminQuestionAddRequest(
                    "Spring Boot란?",
                    "Spring Boot의 핵심 개념과 장점을 설명해주세요.",
                    null, // 카테고리 미구현으로 null 처리
                    true,
                    5

            );

            mockMvc.perform(post("/api/admin/questions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문이 생성되었습니다."))
                    .andExpect(jsonPath("$.data.title").value("Spring Boot란?"))
                    .andExpect(jsonPath("$.data.content").value("Spring Boot의 핵심 개념과 장점을 설명해주세요."))
                    .andExpect(jsonPath("$.data.score").value(5))
                    .andExpect(jsonPath("$.data.isApproved").value(true))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 생성 실패 - 제목 누락")
        void fail1() throws Exception {
            AdminQuestionAddRequest request = new AdminQuestionAddRequest(
                    "", // 제목 누락
                    "내용은 있습니다.",
                    null,
                    true,
                    0
            );

            mockMvc.perform(post("/api/admin/questions")
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
            AdminQuestionAddRequest request = new AdminQuestionAddRequest(
                    "Spring Boot란?",
                    "", //내용 누락
                    null,
                    true,
                    0
            );

            mockMvc.perform(post("/api/admin/questions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("질문 내용은 필수입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 생성 실패 - 점수가 음수")
        void fail3() throws Exception {
            AdminQuestionAddRequest request = new AdminQuestionAddRequest(
                    "Spring Boot란?",
                    "Spring Boot의 핵심 개념과 장점을 설명해주세요.",
                    null,
                    true,
                    -3 // 점수가 음수일때
            );

            mockMvc.perform(post("/api/admin/questions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("점수는 0 이상이어야 합니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("관리자용 질문 수정 API")
    class t2 {

        @Test
        @DisplayName("질문 수정 성공")
        void success() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionUpdateRequest request = new QuestionUpdateRequest(
                    "관리자 수정 제목",
                    "관리자 수정 내용",
                    null // 카테고리 미구현으로 null 처리
            );

            mockMvc.perform(put("/api/admin/questions/{questionId}", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문이 수정되었습니다."))
                    .andExpect(jsonPath("$.data.title").value("관리자 수정 제목"))
                    .andExpect(jsonPath("$.data.content").value("관리자 수정 내용"))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 수정 실패 - 존재하지 않는 ID")
        void fail1() throws Exception {
            Long questionId = 999L;
            QuestionUpdateRequest request = new QuestionUpdateRequest(
                    "수정 제목",
                    "수정 내용",
                    null
            );

            mockMvc.perform(put("/api/admin/questions/{questionId}", questionId)
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
                    null
            );

            mockMvc.perform(put("/api/admin/questions/{questionId}", questionId)
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
                    null
            );

            mockMvc.perform(put("/api/admin/questions/{questionId}", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("질문 내용은 필수입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("관리자용 질문 승인 처리 API")
    class t3 {

        @Test
        @DisplayName("질문 승인 성공")
        void approve() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionApproveRequest request = new QuestionApproveRequest(true);

            mockMvc.perform(patch("/api/admin/questions/{questionId}/approve", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문이 승인 처리되었습니다."))
                    .andExpect(jsonPath("$.data.isApproved").value(true))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 비승인 성공")
        void disapprove() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionApproveRequest request = new QuestionApproveRequest(false);

            mockMvc.perform(patch("/api/admin/questions/{questionId}/approve", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문이 비승인 처리되었습니다."))
                    .andExpect(jsonPath("$.data.isApproved").value(false))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 승인 실패 - 존재하지 않는 질문 ID")
        void fail1() throws Exception {
            Long questionId = 999L;
            QuestionApproveRequest request = new QuestionApproveRequest(true);

            mockMvc.perform(patch("/api/admin/questions/{questionId}/approve", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 승인 실패 - 잘못된 요청 값(null)")
        void fail2() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionApproveRequest request = new QuestionApproveRequest(null);

            mockMvc.perform(patch("/api/admin/questions/{questionId}/approve", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("승인 여부는 필수입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("관리자용 질문 점수 수정 API")
    class t4 {

        @Test
        @DisplayName("질문 점수 수정 성공")
        void success() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionScoreRequest request = new QuestionScoreRequest(20);

            mockMvc.perform(patch("/api/admin/questions/{questionId}/score", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("질문 점수가 수정되었습니다."))
                    .andExpect(jsonPath("$.data.score").value(20))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 점수 수정 실패 - 음수 점수")
        void fail1() throws Exception {
            Long questionId = savedQuestion.getId();
            QuestionScoreRequest request = new QuestionScoreRequest(-5);

            mockMvc.perform(patch("/api/admin/questions/{questionId}/score", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("점수는 0 이상이어야 합니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("질문 점수 수정 실패 - 존재하지 않는 ID")
        void fail2() throws Exception {
            Long questionId = 999L;
            QuestionScoreRequest request = new QuestionScoreRequest(5);

            mockMvc.perform(patch("/api/admin/questions/{questionId}/score", questionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("관리자 질문 조회 API")
    class t5 {

        @Test
        @DisplayName("관리자 질문 목록 조회 성공 - 승인 여부 관계없이 전체 반환")
        void success() throws Exception {
            questionRepository.deleteAll();

            Question approved = questionRepository.save(
                    Question.builder()
                            .title("승인 질문")
                            .content("승인된 질문 내용")
                            .build()
            );
            approved.setApproved(true);

            Question unapproved = questionRepository.save(
                    Question.builder()
                            .title("미승인 질문")
                            .content("미승인 질문 내용")
                            .build()
            );
            unapproved.setApproved(false);

            mockMvc.perform(get("/api/admin/questions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("관리자 질문 목록 조회 성공"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].title").value("승인 질문"))
                    .andExpect(jsonPath("$.data[1].title").value("미승인 질문"))
                    .andDo(print());
        }

        @Test
        @DisplayName("관리자 질문 목록 조회 실패 - 데이터 없음")
        void fail1() throws Exception {
            questionRepository.deleteAll();
            mockMvc.perform(get("/api/admin/questions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("관리자 질문 단건 조회 API")
    class t6 {

        @Test
        @DisplayName("관리자 질문 단건 조회 성공 - 승인 여부 관계없이 조회 가능")
        void success() throws Exception {
            Question question = questionRepository.save(
                    Question.builder()
                            .title("관리자용 단건 조회 질문")
                            .content("관리자는 승인 여부 관계없이 조회 가능")
                            .build()
            );
            question.setApproved(false);

            mockMvc.perform(get("/api/admin/questions/{questionId}", question.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("관리자 질문 단건 조회 성공"))
                    .andExpect(jsonPath("$.data.id").value(question.getId()))
                    .andExpect(jsonPath("$.data.title").value("관리자용 단건 조회 질문"))
                    .andExpect(jsonPath("$.data.content").value("관리자는 승인 여부 관계없이 조회 가능"))
                    .andDo(print());
        }

        @Test
        @DisplayName("관리자 질문 단건 조회 실패 - 존재하지 않는 ID")
        void fail1() throws Exception {
            mockMvc.perform(get("/api/admin/questions/{questionId}", 999L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."))
                    .andDo(print());
        }
    }
}
