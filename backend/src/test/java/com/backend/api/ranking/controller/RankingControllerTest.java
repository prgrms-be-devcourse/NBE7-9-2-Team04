package com.backend.api.ranking.controller;

import com.backend.domain.ranking.entity.Ranking;
import com.backend.domain.ranking.entity.Tier;
import com.backend.domain.ranking.repository.RankingRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private UserRepository userRepository;

    @MockBean
    private Rq rq;

    private User testUser;
    private Ranking testRanking;

    @BeforeEach
    void setUp(){
        testUser = userRepository.save(
                User.builder()
                        .email("testuser@test.com")
                        .password("user1234!")
                        .name("유저")
                        .nickname("user")
                        .age(25)
                        .github("github.com/user")
                        .role(Role.USER)
                        .build());

        testRanking = rankingRepository.save(
                Ranking.builder()
                        .user(testUser)
                        .totalScore(100)
                        .tier(Tier.UNRATED)
                        .rankValue(3)
                        .build()
        );

        when(rq.getUser()).thenReturn(testUser);

    }

    @Nested
    @DisplayName("전체 랭킹 조회 API")
    class t1{

        @Test
        @DisplayName("정상 작동")
        void success1() throws Exception{


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
                    .andExpect(jsonPath("$.message").value("전체 랭킹 조회를 성공했습니다."))
                    .andExpect(jsonPath("$.data.myRanking.userId").value(testUser.getId()))
                    .andExpect(jsonPath("$.data.topRankings").isArray())
                    .andExpect(jsonPath("$.data.topRankings.length()").isNotEmpty())
                    .andExpect(jsonPath("$.data.myRanking.userId").value(testUser.getId()))
                    .andDo(print());

        }

        @Test
        @DisplayName("정상 작동 - 상위 10명만 반환하는지 확인")
        void success2() throws Exception {

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/rankings")
                    .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.topRankings.length()").value(10))
                    .andDo(print());
        }

        @Test
        @DisplayName("전체 랭킹 조회 실패 - 내 랭킹 정보가 없을 때")
        void fail() throws Exception{

            //given
            rankingRepository.deleteAll();

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
                    .andExpect(jsonPath("$.message").value("해당 사용자의 랭킹 정보를 찾을 수 없습니다."))
                    .andDo(print());

        }
    }

    @Nested
    @DisplayName("내 랭킹 조회 API")
    class t2 {

        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {


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
                    .andExpect(jsonPath("$.message").value("내 랭킹 조회를 성공했습니다."))
                    .andExpect(jsonPath("$.data.userId").value(testUser.getId()))
                    .andExpect(jsonPath("$.data.nickName").value("user"))
                    .andExpect(jsonPath("$.data.totalScore").value(100))
                    .andExpect(jsonPath("$.data.currentTier").value("UNRATED"))
                    .andDo(print());

        }

        @Test
        @DisplayName("내 랭킹 조회 실패 = 유저의 랭킹이 존재하지 않을 때")
        void fail() throws Exception {

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
                    .andExpect(jsonPath("$.message").value("해당 사용자의 랭킹 정보를 찾을 수 없습니다."))
                    .andDo(print());

        }
    }
}
