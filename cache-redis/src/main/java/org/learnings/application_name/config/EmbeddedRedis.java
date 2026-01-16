package org.learnings.application_name.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "service.caching-db.embedded",
        havingValue = "true",
        matchIfMissing = true
)
public class EmbeddedRedis {

    public static void main(String[] args) throws IOException, InterruptedException {
        RedisProperties redisProperties = new RedisProperties("localhost", 6379);
        EmbeddedRedis embeddedRedis = new EmbeddedRedis(redisProperties);

        try {
            embeddedRedis.startRedis();
            Thread.sleep(Duration.of(10, ChronoUnit.MINUTES));
        } finally {
            embeddedRedis.stopRedis();
        }
    }

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
            log.info("Starting embedded redis");
            redisServer = new RedisServer(redisProperties.getPort());
            redisServer.start();
        } finally {
            isExecuted = true;
            LOCK.unlock();
        }
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null) {
            log.info("Stopping embedded redis");
            redisServer.stop();
        }
    }
}
