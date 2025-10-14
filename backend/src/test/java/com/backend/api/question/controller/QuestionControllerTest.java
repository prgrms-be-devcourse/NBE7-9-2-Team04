package com.backend.api.question.controller;

import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.domain.question.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

            mockMvc.perform(post("/api/questions")
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
            // given
            QuestionAddRequest request = new QuestionAddRequest(
                    "", // 제목 누락
                    "내용입니다.",
                    null
            );

            // when & then
            mockMvc.perform(post("/api/questions")
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
            // given
            QuestionAddRequest request = new QuestionAddRequest(
                    "Spring Boot란?",
                    "",
                    null
            );

            // when & then
            mockMvc.perform(post("/api/questions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("질문 내용은 필수입니다."))
                    .andDo(print());
        }
    }
}
