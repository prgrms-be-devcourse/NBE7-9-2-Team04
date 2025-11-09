package com.backend.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
@Profile("test")
public class TestRedisConfig {

    private RedisServer redisServer;

    public TestRedisConfig() throws IOException {
        this.redisServer = new RedisServer(6379); // ✅ 테스트 시 사용할 포트
    }

    @PostConstruct
    public void startRedis() {
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}