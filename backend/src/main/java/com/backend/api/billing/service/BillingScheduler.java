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

    //00시에 자동으로 결제 진행
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void runAutoBillingTask(){

        //오늘 결제일인 구독 목록 조회
        List<Subscription> subscriptions =
                subscriptionRepository.findByNextBillingDateAndIsActive(LocalDate.now(), true);


        for(Subscription subscription:subscriptions){
            if(subscription.getBillingKey() == null){
                subscription.deActivatePremium(); //구독 종료
                subscriptionRepository.save(subscription);
                continue;
            }
            try{
                billingService.autoPayment(subscription);
            }catch(ErrorException e){
                subscription.deActivatePremium();
                subscriptionRepository.save(subscription);
            }

        }
    }
}
