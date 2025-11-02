package com.backend.api.billing.controller;


import com.backend.api.billing.dto.request.BillingRequest;
import com.backend.api.billing.dto.response.BillingResponse;
import com.backend.api.billing.service.BillingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

    @MockitoBean
    private BillingService billingService;

    private User user;

    @BeforeEach
    void setUp(){
        user = userRepository.save(User.builder()
                .email("user@test.com")
                .password("user1234!")
                .name("유저1")
                .nickname("user")
                .age(25)
                .github("github.com/user")
                .role(Role.USER)
                .build());
    }

    @Nested
    @DisplayName("빌링키 발급 API")
    class t1{

        @Test
        @DisplayName("빌링 키 발급 성공")
        void success() throws Exception{

            //given
            BillingRequest request = new BillingRequest("customerKey123", "authKey123");
            BillingResponse response = new BillingResponse("billingKey123", "customerKey123");

            when(billingService.issueBillingKey(any(BillingRequest.class)))
                    .thenReturn(response);

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
    }

}
