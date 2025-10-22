package com.backend.domain.subscription.entity;


import com.backend.domain.payment.entity.Payment;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(nullable = false)
    private LocalDate nextBillingDate;

    //질문 가능 횟수
    //무료 회원 5번
    //유료 회원 8번
    @Column(nullable = false)
    private int questionLimit;


    //구독 이름
    @Column(nullable = false)
    private String subscriptionName;

    //구독 가격
    @Column(nullable = false)
    private Long price;

    // Billing 정보 (결제 수단)
    @Column(nullable = true, unique = true)
    private String billingKey;

    //가입 시점에서 생성
    @Column(nullable = false, unique = true)
    private String customerKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    public void activatePremium(String billingKey) {
        this.billingKey = billingKey;
        this.isActive = true;
        this.subscriptionType = SubscriptionType.PREMIUM;
        this.subscriptionName = "PREMIUM";
        this.price = 9900L;
        this.questionLimit = 8;
        this.startDate = LocalDateTime.now();
        this.endDate = this.startDate.plusMonths(1);
        this.nextBillingDate = LocalDate.now().plusMonths(1);
    }

    @PrePersist
    public void generateCustomerKey() {
        if (this.customerKey == null) {
            this.customerKey = UUID.randomUUID().toString();
        }
    }


//    public void activate(LocalDateTime endDate) {
//        this.endDate = endDate;
//        this.isActive = true;
//    }
//
//    public void deactivate() {
//        this.isActive = false;
//    }



}
