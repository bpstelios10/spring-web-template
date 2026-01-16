package org.learnings.application_name.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("service.caching-db")
@AllArgsConstructor
@NoArgsConstructor
public class RedisProperties {
    private String host;
    private int port;
}
