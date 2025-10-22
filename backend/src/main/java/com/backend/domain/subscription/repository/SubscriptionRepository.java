package com.backend.domain.subscription.repository;

import com.backend.domain.subscription.entity.Subscription;
import com.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByUserAndIsActiveTrue(User user);
    Optional<Subscription> findByCustomerKey(String customerKey);
    Optional<Subscription> findByUser(User user);
}
