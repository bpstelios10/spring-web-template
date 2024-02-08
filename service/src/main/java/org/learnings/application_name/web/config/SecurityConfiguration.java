package org.learnings.application_name.web.config;

import org.learnings.application_name.web.properties.BasicAuthEndpoint;
import org.learnings.application_name.web.properties.BasicAuthProperties;
import org.learnings.application_name.web.properties.BasicAuthUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final BasicAuthProperties basicAuthProperties;
    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfiguration(BasicAuthProperties basicAuthProperties,
                                 CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint,
                                 CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.basicAuthProperties = basicAuthProperties;
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, UserDetailsService userDetailsService) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint))
                .exceptionHandling(excHandler -> excHandler.accessDeniedHandler(customAccessDeniedHandler))
                .userDetailsService(userDetailsService);

        for (BasicAuthEndpoint securedEndpoint : basicAuthProperties.getSecuredEndpoints()) {
            httpSecurity.authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.valueOf(securedEndpoint.getMethod()), securedEndpoint.getUrlPattern())
                    .hasRole(securedEndpoint.getRole())
            );
        }
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(BasicAuthProperties basicAuthProperties) {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        for (BasicAuthUser userProperties : basicAuthProperties.getUsers()) {
            UserDetails user = User.builder()
                    .username(userProperties.getUsername())
                    .password(passwordEncoder().encode(userProperties.getPassword()))
                    .roles(userProperties.getRole())
                    .build();
            inMemoryUserDetailsManager.createUser(user);
        }

        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4, new SecureRandom());
    }
}
