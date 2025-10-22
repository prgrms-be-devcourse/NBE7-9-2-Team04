package com.backend.domain.subscription.entity;

import com.backend.domain.payment.entity.Payment;
import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    //무료 회원 5번
    //유료 회원 8번
    @Column(nullable = false)
    private int questionLimit;

    // 빌링 키 (정기 결제 시 필요. 일단은 주석 처리)
//    @Column(name = "billing_key", unique = true, length = 100)
//    private String billingKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
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

    // 구독 유효성 검증
    public boolean isValid() {
        return this.isActive && this.endDate.isAfter(LocalDateTime.now());
    }

}
