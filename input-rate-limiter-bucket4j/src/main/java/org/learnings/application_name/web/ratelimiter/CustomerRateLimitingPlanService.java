package org.learnings.application_name.web.ratelimiter;

import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerRateLimitingPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRateLimitingPlanService.class);

    private final BucketStore<String, Bucket> buckets;

    public CustomerRateLimitingPlanService(BucketStore<String, Bucket> buckets) {
        this.buckets = buckets;
    }

    public Bucket resolveBucketForCustomer(String apiKey) {
        return buckets.get(apiKey).orElseGet(() -> {
            Bucket bucket = createBucketForCustomer(apiKey);
            return buckets.put(apiKey, bucket);
        });
    }

    private Bucket createBucketForCustomer(String apiKey) {
        CustomerRateLimitingPlan plan = CustomerRateLimitingPlan.getPlan(apiKey);
        LOG.debug("******** Rate Limit Plan: {}", plan);

        return Bucket.builder()
                .addLimit(plan.getLimit())
                .build();
    }
}
