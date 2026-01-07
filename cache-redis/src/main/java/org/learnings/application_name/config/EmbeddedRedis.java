package org.learnings.application_name.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import redis.embedded.RedisServer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class EmbeddedRedis {

    private static volatile boolean isExecuted = false;
    private static final Lock LOCK = new ReentrantLock();

    private final RedisProperties redisProperties;
    @Getter
    private RedisServer redisServer;

    public EmbeddedRedis(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @PostConstruct
    public void startRedis() throws IOException {
        // lock to prevent parallel threads. Check if embedded redis is already being executed
        LOCK.lock();
        if (isExecuted) {
            LOCK.unlock();
            return;
        }

        try {
            redisServer = new RedisServer(redisProperties.getRedisPort());
            redisServer.start();
        } finally {
            isExecuted = true;
            LOCK.unlock();
        }
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
