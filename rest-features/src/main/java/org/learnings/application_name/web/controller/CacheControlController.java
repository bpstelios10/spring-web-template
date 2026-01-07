package org.learnings.application_name.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("cache-control/")
public class CacheControlController {

    private static final AtomicInteger counter1 = new AtomicInteger();
    private static final AtomicInteger counter2 = new AtomicInteger();
    private static final AtomicInteger counter3 = new AtomicInteger();

    @GetMapping("max-age/current-request")
    public ResponseEntity<Integer> getResourceCachedMaxAge() {
        log.debug("requested resource, using max-age cache-control");
        int currentRequest = counter1.incrementAndGet();

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(90, TimeUnit.SECONDS).cachePrivate())
                .body(currentRequest);
    }

    @GetMapping("no-cache/current-request")
    public ResponseEntity<Integer> getResourceCachedNoCache() {
        log.debug("requested resource, using no-cache cache-control");
        int currentRequest = counter2.incrementAndGet();

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(currentRequest);
    }

    @GetMapping("filter-cache-control/current-request")
    public ResponseEntity<Integer> getResourceCachedFromFilter() {
        log.debug("requested resource, using generic filter for cache-control");
        int currentRequest = counter3.incrementAndGet();

        return ResponseEntity
                .ok(currentRequest);
    }
}
