package com.backend.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@RedisHash(value = "refreshToken") //RedisTemplate에서는 필수는 아니지만 일단 사용
public class RefreshToken {

    @Id
    private Long userId;

    private String refreshToken;

    @TimeToLive
    private Long expiration;
}
