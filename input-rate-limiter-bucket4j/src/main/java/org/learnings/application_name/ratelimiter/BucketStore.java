package org.learnings.application_name.ratelimiter;

import java.util.Optional;

public interface BucketStore<K, V> {
    Optional<V> get(K key);
    V put(K key, V value);
    void evict(K key);
}
