package org.learnings.application_name.web.ratelimiter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(
        name = "service.rate-limiter.store",
        havingValue = "db"
)
public class SharedBucketStoreImpl<K, V> implements BucketStore<K, V> {

    @Override
    public Optional<V> get(K key) {
        return Optional.empty();
    }

    @Override
    public V put(K key, V value) {
        // read from DB
        return value;
    }

    @Override
    public void evict(K key) {
        // read from DB
    }
}
