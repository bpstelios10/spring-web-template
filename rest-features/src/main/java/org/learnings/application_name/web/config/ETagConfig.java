package org.learnings.application_name.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class ETagConfig {

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> eTagFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/etag/resource1/strong/*");
        filterRegistrationBean.setName("strongETagFilter");

        return filterRegistrationBean;
    }
}
