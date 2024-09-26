package org.learnings.application_name.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "service.basic-auth")
public class BasicAuthProperties {
    private Set<BasicAuthEndpoint> securedEndpoints = new HashSet<>();
    private Set<BasicAuthUser> users = new HashSet<>();
}
