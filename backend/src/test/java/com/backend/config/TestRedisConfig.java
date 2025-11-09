package com.backend.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.ServerSocket;

@TestConfiguration
public class TestRedisConfig {

    private RedisServer redisServer;
    private int port;

    @PostConstruct
    public void startRedis() throws IOException {
        // 1️⃣ 먼저 포트 확인
        port = 6379;
        if (isPortInUse(port)) {
            port = findAvailablePort();
        }

        // 2️⃣ Redis 실행
        redisServer = new RedisServer(port);
        redisServer.start();

        // 3️⃣ Spring에 동적으로 port 반영
        System.setProperty("spring.data.redis.port", String.valueOf(port));
        System.setProperty("spring.data.redis.host", "localhost");

        System.out.println("✅ Embedded Redis started on port " + port);
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            System.out.println("🛑 Embedded Redis stopped");
        }
    }

    private boolean isPortInUse(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private int findAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}