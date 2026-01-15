package org.learnings.application_name.web.ratelimiter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class CustomerRateLimitingInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRateLimitingInterceptor.class);

    private final CustomerRateLimitingPlanService customerRateLimitingPlanService;

    public CustomerRateLimitingInterceptor(CustomerRateLimitingPlanService customerRateLimitingPlanService) {
        this.customerRateLimitingPlanService = customerRateLimitingPlanService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws IOException {
        String apiKey = request.getHeader("X-api-key");
        LOG.debug("******** API Key: {}", apiKey);

        if (apiKey == null || apiKey.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("{  \"error\": \"Missing Header: X-api-key\"  }");
            return false;
        }

        Bucket tokenBucket = customerRateLimitingPlanService.resolveBucketForCustomer(apiKey);
        LOG.debug("******** Bucket: {}", tokenBucket);
        LOG.debug("******** Bucket Hash: {}", tokenBucket.hashCode());
        LOG.debug("******** Bucket available tokens: {}", tokenBucket.getAvailableTokens());

        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("{  \"error\": \"You have exhausted your API Request Quota\"  }");
            return false;
        }

        LOG.debug("********* Released token");
        LOG.debug("********* Bucket available tokens: {}", tokenBucket.getAvailableTokens());

        return true;
    }
}
