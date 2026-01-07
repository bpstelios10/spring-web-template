package org.learnings.application_name.web.config;

import org.learnings.application_name.web.filters.CacheControlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CacheControlFilter> registerCacheControlFilter() {
        FilterRegistrationBean<CacheControlFilter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new CacheControlFilter());
        filterRegistrationBean.addUrlPatterns("/cache-control/filter-cache-control/current-request/*");
        filterRegistrationBean.setName("cacheControlFilter");
        filterRegistrationBean.setOrder(10);

        return filterRegistrationBean;
    }
}
