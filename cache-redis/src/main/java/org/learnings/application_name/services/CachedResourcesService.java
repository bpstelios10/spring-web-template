package org.learnings.application_name.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CachedResourcesService {

    private final AtomicInteger counter1 = new AtomicInteger();
    private final AtomicInteger counter2 = new AtomicInteger();
    private final AtomicInteger counter3 = new AtomicInteger();

    @Cacheable("resource1")
    public int getCounter1() {
        return counter1.incrementAndGet();
    }

    @Cacheable("resource2")
    public int getCounter2() {
        return counter2.incrementAndGet();
    }

    @CacheEvict(value = "resource2", allEntries = true)
    public void refreshCounter2() {
    }

    public int getCounter3() {
        return counter3.incrementAndGet();
    }
}
