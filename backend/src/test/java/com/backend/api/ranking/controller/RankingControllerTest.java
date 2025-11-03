package com.backend.api.ranking.controller;

import com.backend.api.subscription.controller.SubscriptionController;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.domain.ranking.repository.RankingRepository;
import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.entity.SubscriptionType;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.domain.userQuestion.repository.UserQuestionRepository;
import com.backend.global.Rq.Rq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
public class RankingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private UserQuestionRepository userQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private Rq rq;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp(){
        testUser1 = userRepository.save(
                User.builder()
                        .email("testuser1@test.com")
                        .password("user1234!")
                        .name("유저1")
                        .nickname("user1")
                        .age(25)
                        .github("github.com/user1")
                        .role(Role.USER)
                        .build());


        testUser2 = userRepository.save(
                User.builder()
                        .email("testuser2@test.com")
                        .password("user1234!")
                        .name("유저2")
                        .nickname("user2")
                        .age(25)
                        .github("github.com/user2")
                        .role(Role.USER)
                        .build());

    }

    @Nested
    @DisplayName("전체 랭킹 조회 API")
    class t1{

        @Test
        @DisplayName("정상 작동")
        void success() throws Exception{

            //given
            when(rq.getUser()).thenReturn(testUser1);

            //when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/rankings")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)

            );

            //then
            resultActions
                    .andExpect(handler().handlerType(RankingController.class))
                    .andExpect(handler().methodName("getRankings"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("구독 정보를 불러왔습니다."))
                    .andExpect(jsonPath("$.data.userId").value(testUser1.getId()))
                    .andExpect(jsonPath("$.data.subscriptionName").value("PREMIUM"))
                    .andExpect(jsonPath("$.data.subscriptionType").value("PREMIUM"))
                    .andExpect(jsonPath("$.data.isActive").value(true))
                    .andExpect(jsonPath("$.data.price").value(9900))
                    .andExpect(jsonPath("$.data.customerKey").value("customerKey123"))
                    .andExpect(jsonPath("$.data.billingKey").value("billingKey123"))
                    .andDo(print());

        }

        @Test
        @DisplayName("전체 랭킹 조회 실패 - 상위 10명 랭킹 정보가 없을 때")
        void fail() throws Exception{

            //given
            User newUser = userRepository.save(
                    User.builder()
                            .email("newuser@test.com")
                            .password("user1234!")
                            .name("새유저")
                            .nickname("새유저")
                            .age(25)
                            .github("github.com/newUser")
                            .role(Role.USER)
                            .build());

            when(rq.getUser()).thenReturn(newUser);


            //when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/rankings")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)

            );

            //then
            resultActions
                    .andExpect(handler().handlerType(RankingController.class))
                    .andExpect(handler().methodName("getRankings"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("해당 고객의 구독 정보를 찾을 수 없습니다."))
                    .andDo(print());

        }
    }

    @Nested
    @DisplayName("내 랭킹 조회 API")
    class t2{

        @Test
        @DisplayName("정상 작동")
        void success() throws Exception{

            //given
            when(rq.getUser()).thenReturn(testUser1);

            //when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/rankings/me")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)

            );

            //then
            resultActions
                    .andExpect(handler().handlerType(RankingController.class))
                    .andExpect(handler().methodName("getMyRankingOnly"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("구독 정보를 불러왔습니다."))
                    .andExpect(jsonPath("$.data.userId").value(testUser1.getId()))
                    .andExpect(jsonPath("$.data.subscriptionName").value("PREMIUM"))
                    .andExpect(jsonPath("$.data.subscriptionType").value("PREMIUM"))
                    .andExpect(jsonPath("$.data.isActive").value(true))
                    .andExpect(jsonPath("$.data.price").value(9900))
                    .andExpect(jsonPath("$.data.customerKey").value("customerKey123"))
                    .andExpect(jsonPath("$.data.billingKey").value("billingKey123"))
                    .andDo(print());

        }

        @Test
        @DisplayName("내 랭킹 조회 실패 = 유저의 랭킹이 존재하지 않을 때")
        void fail() throws Exception{

            //given
            User newUser = userRepository.save(
                    User.builder()
                            .email("newuser@test.com")
                            .password("user1234!")
                            .name("새유저")
                            .nickname("새유저")
                            .age(25)
                            .github("github.com/newUser")
                            .role(Role.USER)
                            .build());

            when(rq.getUser()).thenReturn(newUser);


            //when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/rankings/me")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)

            );

            //then
            resultActions
                    .andExpect(handler().handlerType(RankingController.class))
                    .andExpect(handler().methodName("getMyRankingOnly"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("해당 고객의 구독 정보를 찾을 수 없습니다."))
                    .andDo(print());

        }
}
