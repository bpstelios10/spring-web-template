package org.learnings.application_name.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RedisProperties {
    private final int redisPort;
    private final String redisHost;

    public RedisProperties(
            @Value("${redis.port}") int redisPort,
            @Value("${redis.host}") String redisHost) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }
}
