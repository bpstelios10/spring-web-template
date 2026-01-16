package org.learnings.application_name.ratelimiter;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(
        name = "service.rate-limiter.store",
        havingValue = "db"
)
public class SharedBucketStoreImpl<K, V> implements BucketStore<K, V> {

    private final ProxyManager<String> proxyManager;

    public SharedBucketStoreImpl(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Override
    public Optional<V> get(K key) {
        CustomerRateLimitingPlan plan = CustomerRateLimitingPlan.getPlan((String) key);

        BucketConfiguration bucketConfiguration = BucketConfiguration.builder()
                .addLimit(plan.getLimit())
                .build();
        BucketProxy bucketProxy = proxyManager.builder().build((String) key, bucketConfiguration);
        return Optional.ofNullable((V) bucketProxy);
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void evict(K key) {
        // delete from DB
    }
}
