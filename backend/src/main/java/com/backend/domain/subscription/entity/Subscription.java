package com.backend.domain.subscription.entity;


import com.backend.domain.billing.entity.Billing;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
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
    //무료 회원 3번
    //유료 회원 5번
    @Column(nullable = false)
    private int questionLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_key_id", nullable = false)
    private Billing billing; // 결제용 빌링키


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();


    public void activate(LocalDateTime endDate) {
        this.endDate = endDate;
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }



}
