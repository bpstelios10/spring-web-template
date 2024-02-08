package org.learnings.application_name.web.properties;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BasicAuthUser {
    private String username;
    private String password;
    private String role;
}
