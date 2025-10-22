package com.backend.api.subscription.service;

import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.entity.SubscriptionType;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final Rq rq;

    private static final long FIXED_SUBSCRIPTION_PRICE = 9900L; //가격 9900으로 통일

    @Transactional
    public Subscription createSubscription(SubscriptionType type) {
        User user = rq.getUser();

        subscriptionRepository.findByUser(user).ifPresent(sub -> {
            throw new ErrorException(ErrorCode.SUBSCRIPTION_ALREADY_EXISTS);
        });

        String customerKey = "user_" + UUID.randomUUID();

        Subscription subscription = Subscription.builder()
                .user(user)
                .subscriptionType(type)
                .subscriptionName("PREMIUM")
                .price(FIXED_SUBSCRIPTION_PRICE) // ✅ 항상 9,900원
                .isActive(false)
                .questionLimit(8) // ✅ 유료 회원 질문 제한
                .customerKey(customerKey)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .nextBillingDate(LocalDate.now().plusMonths(1))
                .build();

        return subscriptionRepository.save(subscription);

    }

    // ✅ 현재 로그인한 사용자의 구독 조회
    public Subscription getMySubscription() {
        User user = rq.getUser();

        return subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new ErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }
}
