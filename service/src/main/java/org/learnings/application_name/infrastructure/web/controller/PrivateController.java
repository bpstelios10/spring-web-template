package org.learnings.application_name.infrastructure.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping(path = "/status")
    public ResponseEntity<String> status() {
        log.info("test logs");

        return ResponseEntity.ok("OK");
    }
//
//    @GetMapping(path = "/readinessCustomCheck")
//    public ResponseEntity<String> readinessCustomCheck() {
//        log.info("checking readiness");
//
//        String getTime = "SELECT toTimestamp(now()) FROM system.local";
//        DefaultRow one = cassandraTemplate.selectOne(getTime, DefaultRow.class);
//
//        if (one != null && one.getInstant(0) != null) log.info(String.valueOf(one.getInstant(0)));
//        else log.info("'SELECT now() FROM system.local' didnt give a response back");
//
//        return ResponseEntity.ok("OK");
//    }
}
