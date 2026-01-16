package org.learnings.application_name.web.configuration;

import org.learnings.application_name.ratelimiter.CustomerRateLimitingInterceptor;
import org.learnings.application_name.ratelimiter.CustomerRateLimitingPlanService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final CustomerRateLimitingPlanService customerRateLimitingPlanService;

    public AppConfig(CustomerRateLimitingPlanService customerRateLimitingPlanService) {
        this.customerRateLimitingPlanService = customerRateLimitingPlanService;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new CustomerRateLimitingInterceptor(customerRateLimitingPlanService))
                .addPathPatterns("/resource1/rate-limited-resources/**")
                .order(5);
    }
}
