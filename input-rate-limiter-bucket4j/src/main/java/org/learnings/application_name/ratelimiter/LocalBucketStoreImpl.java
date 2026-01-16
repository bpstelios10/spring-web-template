package org.learnings.application_name.ratelimiter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(
        name = "service.rate-limiter.store",
        havingValue = "local",
        matchIfMissing = true
)
public class LocalBucketStoreImpl<K, V> implements BucketStore<K, V> {

    private final Map<K, V> store = new ConcurrentHashMap<>();

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(store.get(key));
    }

    /** putIfAbsent returns null, if the new value is applied. we will always return:
     * - either the retrieved value
     * - or the applied one. never null (unless it is the value. highly improbable)
    */
    @Override
    public V put(K key, V value) {
        V ifAbsent = store.putIfAbsent(key, value);
        return ifAbsent == null ? value : ifAbsent;
    }

    @Override
    public void evict(K key) {
        store.remove(key);
    }
}
