package com.backend.api.subscription.service;

import com.backend.api.subscription.dto.response.SubscriptionResponse;
import com.backend.api.user.service.UserService;
import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    @Transactional
    public SubscriptionResponse activatePremium(String customerKey, String billingKey) {
        Subscription subscription = subscriptionRepository.findByCustomerKey(customerKey)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        subscription.activatePremium(billingKey);
        subscriptionRepository.save(subscription);
        return SubscriptionResponse.from(subscription);
    }

    @Transactional
    public SubscriptionResponse getSubscriptionByUserId(Long userId) {
        User user = userService.getUser(userId);

        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        return SubscriptionResponse.from(subscription);

    }

    @Transactional(readOnly = true)
    public Subscription getSubscriptionByCustomerKey(String customerKey) {

        return subscriptionRepository.findByCustomerKey(customerKey)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    @Transactional
    public void updateNextBillingDate(Subscription subscription, LocalDate nextDate) {
        subscription.updateNextBillingDate(nextDate);
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public SubscriptionResponse cancelSubscription(String customerKey) {

        Subscription subscription = subscriptionRepository.findByCustomerKey(customerKey)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        if(!subscription.isActive()) { //이미 비활성화된 구독은 처리 x
            throw new ErrorException(ErrorCode.SUBSCRIPTION_INACTIVE);
        }

        subscription.setBillingKey(null);

        //다음 결제일까지는 구독 유효하도록
        subscriptionRepository.save(subscription);

        return SubscriptionResponse.from(subscription);
    }


}
