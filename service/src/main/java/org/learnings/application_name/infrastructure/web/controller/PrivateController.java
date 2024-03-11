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
}
