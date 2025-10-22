package com.backend.api.billing.service;


import com.backend.api.billing.dto.request.BillingRequest;
import com.backend.api.billing.dto.response.BillingConfirmResponse;
import com.backend.api.billing.dto.response.BillingResponse;
import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final SubscriptionRepository subscriptionRepository;
    private final WebClient webClient;

    //빌링키 발급
    @Transactional
    public BillingResponse issueBillingKey(BillingRequest request) {

        Map<String, Object> body = Map.of(
                "authKey", request.authKey(),
                "customerKey", request.customerKey()
        );

        BillingConfirmResponse response = webClient.post()
                .uri("/billing/authorizations/issue")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody ->
                                        Mono.error(new ErrorException(errorBody, ErrorCode.PAYMENT_APPROVE_FAILED))
                                )
                )
                .bodyToMono(BillingConfirmResponse.class)
                .block();

        if (response == null || response.billingKey() == null) {
            throw new ErrorException(ErrorCode.PAYMENT_APPROVE_FAILED);
        }

        Subscription subscription = subscriptionRepository.findByCustomerKey(response.customerKey())
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        subscription.updateBillingInfo(response.billingKey(), response.customerKey());
        subscriptionRepository.save(subscription);

        return BillingResponse.from(subscription);
    }
}
