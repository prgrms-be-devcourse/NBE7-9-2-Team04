package com.backend.domain.user.entity;

import com.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@RedisHash(value = "refreshToken", timeToLive = 604800) //7일
public class RefreshToken extends BaseEntity {

    private String refreshToken;
}
