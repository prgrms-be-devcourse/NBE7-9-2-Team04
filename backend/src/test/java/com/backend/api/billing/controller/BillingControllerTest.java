package com.backend.api.billing.controller;


import com.backend.api.billing.dto.request.BillingRequest;
import com.backend.api.billing.dto.response.BillingResponse;
import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.entity.SubscriptionType;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
public class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @MockitoBean
    private WebClient webClient;

    private User testUser;
    private Subscription subscription;

    @BeforeEach
    void setUp(){
        testUser = userRepository.save(User.builder()
                .email("user@test.com")
                .password("user1234!")
                .name("유저1")
                .nickname("user")
                .age(25)
                .github("github.com/user")
                .role(Role.USER)
                .build());

        subscription = subscriptionRepository.save(
                Subscription.builder()
                        .user(testUser)
                        .subscriptionType(SubscriptionType.BASIC)
                        .isActive(false)
                        .subscriptionName("BASIC")
                        .price(0L)
                        .questionLimit(5)
                        .startDate(LocalDateTime.now())
                        .build()
        );
    }

    @Nested
    @DisplayName("빌링키 발급 API")
    class t1{

        @Test
        @DisplayName("빌링 키 발급 성공 - 수정해야함")
        void success() throws Exception{

            //given
            BillingRequest request = new BillingRequest("customerKey123", "authKey123");
            BillingResponse response = new BillingResponse("billingKey123", "customerKey123");


            //when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/billing/confirm")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))

            );

            //then
            resultActions
                    .andExpect(handler().handlerType(BillingController.class))
                    .andExpect(handler().methodName("issueBillingKey"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("빌링키가 발급되었습니다."))
                    .andExpect(jsonPath("$.data.billingKey").value("billingKey123"))
                    .andExpect(jsonPath("$.data.customerKey").value("customerKey123"))
                    .andDo(print());
        }

        @Test
        @DisplayName("빌링 키 발급 실패 - 누락된 customerKey")
        void fail1() throws Exception{

            //given
            BillingRequest invalidRequest = new BillingRequest(null, "authKey123");

            //when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/billing/confirm")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest))

            );

            //then
            resultActions
                    .andExpect(handler().handlerType(BillingController.class))
                    .andExpect(handler().methodName("issueBillingKey"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("CUSTOMER_KEY가 누락되었거나 유효하지 않습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("빌링 키 발급 실패 - 누락된 authKey")
        void fail2() throws Exception{

            //given
            BillingRequest invalidRequest = new BillingRequest(subscription.getCustomerKey(), null);

            //when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/billing/confirm")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest))

            );

            //then
            resultActions
                    .andExpect(handler().handlerType(BillingController.class))
                    .andExpect(handler().methodName("issueBillingKey"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("AUTH_KEY가 누락되었거나 유효하지 않습니다."))
                    .andDo(print());
        }
    }

}
