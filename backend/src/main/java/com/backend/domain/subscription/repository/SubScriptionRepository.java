package com.backend.domain.subscription.repository;

import com.backend.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubScriptionRepository extends JpaRepository<Subscription, Long> {
}
