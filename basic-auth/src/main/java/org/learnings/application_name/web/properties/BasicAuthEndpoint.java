package org.learnings.application_name.web.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthEndpoint {
    private String method;
    private String urlPattern;
    private String role;
}
