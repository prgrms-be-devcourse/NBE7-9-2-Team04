package com.backend.api.subscription.service;

import com.backend.api.subscription.dto.response.SubscriptionResponse;
import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final Rq rq;

    private static final long FIXED_SUBSCRIPTION_PRICE = 9900L; //가격 9900으로 통일

    @Transactional
    public SubscriptionResponse activatePremium(String customerKey, String billingKey) {
        Subscription subscription = subscriptionRepository.findByCustomerKey(customerKey)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        subscription.activatePremium(billingKey);
        subscriptionRepository.save(subscription);
        return SubscriptionResponse.from(subscription);
    }


    public SubscriptionResponse getMySubscription() {
        User user = rq.getUser();

        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        return SubscriptionResponse.from(subscription);
    }
}
