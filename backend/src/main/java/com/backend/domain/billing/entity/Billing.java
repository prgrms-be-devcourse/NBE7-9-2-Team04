package com.backend.domain.billing.entity;

import com.backend.domain.user.entity.User;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Billing extends BaseEntity {

    //토스 발급 빌링 키
    @Column(nullable = false, unique = true)
    private String billingKey;

    // 우리 서비스 내 고객 식별자
    @Column(nullable = false, unique = true)
    private String customerKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true) //한 사람 당 하나의 빌링키
    private User user;

    private boolean active = true; // 사용 여부


}
