package com.backend.domain.subscription.entity;


import com.backend.domain.payment.entity.Payment;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Subscription extends BaseEntity {

    //구독 유형
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType subscriptionType;

    //구독 활성화 여부
    @Column(nullable = false)
    private boolean isActive;

    //구독 시작 날짜
    @Column(nullable = false)
    private LocalDateTime startDate;

    //구독 만료 날짜
    @Column(nullable = false)
    private LocalDateTime endDate;

    //질문 가능 횟수
    @Column(nullable = false)
    private int questionLimit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "subscription", fetch = FetchType.LAZY)
    private Payment payment;


    public void activate(LocalDateTime endDate) {
        this.endDate = endDate;
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }



}
