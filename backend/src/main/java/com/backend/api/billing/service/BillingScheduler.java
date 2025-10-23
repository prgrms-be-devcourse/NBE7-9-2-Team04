package com.backend.api.billing.service;

import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.subscription.repository.SubscriptionRepository;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BillingScheduler {

    private final BillingService billingService;
    private final SubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void runAutoBillingTask(){

        //오늘 결제일인 구독 목록 조회
        List<Subscription> subscriptions =
                subscriptionRepository.findByNextBillingDateAndIsActive(LocalDate.now(), true);

        subscriptions.forEach(subscription -> {
            try {
                billingService.autoPayment(subscription);
            } catch (ErrorException e) {
                subscription.deactivate();
            }
        });
    }
}
