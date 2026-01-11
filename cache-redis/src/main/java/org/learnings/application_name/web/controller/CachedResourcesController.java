package org.learnings.application_name.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.services.CachedResourcesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("cached")
public class CachedResourcesController {

    private final CachedResourcesService service;

    public CachedResourcesController(CachedResourcesService service) {
        this.service = service;
    }

    @GetMapping("/without-evict")
    public ResponseEntity<Integer> getCachedEndpoint1() {
        log.debug("requested cached resource 1");
        int currentRequest = service.getCounter1();

        return ResponseEntity
                .ok()
                .body(currentRequest);
    }

    @GetMapping("/with-evict")
    public ResponseEntity<Integer> getCachedEndpoint2() {
        log.debug("requested cached resource 2");
        int currentRequest = service.getCounter2();

        return ResponseEntity
                .ok()
                .body(currentRequest);
    }

    @DeleteMapping("/resource2/cache")
    public ResponseEntity<Void> postEvictResource2() {
        log.debug("requested evict cache of resource 2");

        service.refreshCounter2();

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/no-cached")
    public ResponseEntity<Integer> getCachedEndpoint3() {
        log.debug("requested cached resource 3");
        int currentRequest = service.getCounter3();

        return ResponseEntity
                .ok()
                .body(currentRequest);
    }
}
