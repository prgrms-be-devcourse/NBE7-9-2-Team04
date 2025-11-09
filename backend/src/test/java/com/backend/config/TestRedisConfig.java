package com.backend.config;


import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.ServerSocket;

@TestConfiguration
public class TestRedisConfig {

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {

        int port = findAvailablePort();

        redisServer = new RedisServer(port);
        redisServer.start();

        System.setProperty("spring.data.redis.port", String.valueOf(port));
        System.setProperty("spring.data.redis.host", "localhost");

    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    private int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new ErrorException(ErrorCode.REDIS_ERROR);
        }
    }
}