package org.learnings.application_name.web.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({BasicAuthProperties.class})
public class ServiceProperties {
}
