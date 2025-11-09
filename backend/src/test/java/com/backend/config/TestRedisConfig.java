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

    public TestRedisConfig() throws IOException {
        this.port = findAvailablePort();
        this.redisServer = new RedisServer(port);
    }

    private int findAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    @PostConstruct
    public void start() {
        redisServer.start();
        System.setProperty("spring.data.redis.port", String.valueOf(port));
        System.setProperty("spring.data.redis.host", "localhost");
    }

    @PreDestroy
    public void stop() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}