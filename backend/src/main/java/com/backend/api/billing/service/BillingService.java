package com.backend.api.billing.service;


import com.backend.api.billing.dto.request.BillingRequest;
import com.backend.api.billing.dto.response.BillingResponse;
import com.backend.api.subscription.dto.response.SubscriptionResponse;
import com.backend.api.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {

    private final SubscriptionService subscriptionService;
    private final WebClient webClient;

    //빌링키 발급
    @Transactional
    public BillingResponse issueBillingKey(BillingRequest request) {

        Map<String, Object> billingResponse = webClient.post()
                .uri("v1/billing/authorizations/issue")
                .bodyValue(Map.of(
                        "authKey", request.authKey(),
                        "customerKey", request.customerKey()
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        String billingKey = (String) billingResponse.get("billingKey");

        // 구독 PREMIUM 전환
        SubscriptionResponse updated = subscriptionService.activatePremium(request.customerKey(), billingKey);

        return new BillingResponse(billingKey, updated.customerKey());
    }
}
