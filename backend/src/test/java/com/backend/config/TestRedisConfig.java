package com.backend.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class TestRedisConfig {

    private RedisServer redisServer;

    @Value("${spring.data.redis.port}")
    private int port;

    @PostConstruct
    public void startRedis() throws IOException {

        redisServer = new RedisServer(port);
        redisServer.start();

        System.setProperty("spring.data.redis.port", String.valueOf(port));
        System.setProperty("spring.data.redis.host", "localhost");

    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}