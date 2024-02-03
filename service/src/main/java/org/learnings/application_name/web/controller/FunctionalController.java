package org.learnings.application_name.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.model.FunctionalResource;
import org.learnings.application_name.services.FunctionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("resource1")
public class FunctionalController {

    private final FunctionalService functionalService;

    public FunctionalController(FunctionalService functionalService) {
        this.functionalService = functionalService;
    }

    @GetMapping
    public ResponseEntity<List<FunctionalResource>> getAllResource1() {
        log.debug("requested all resources");

        return ResponseEntity.ok(functionalService.getAllResource1());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunctionalResource> getResource1ByID(@PathVariable String id) {
        log.debug("requested resource with id [{}]", id);
        //add check to validate id is uuid
        UUID requestID = UUID.fromString(id);

        return ResponseEntity.ok(functionalService.getResource1ByID(requestID).orElse(null));
    }

    @PostMapping
    public ResponseEntity<Void> createResource1(@Valid @RequestBody CreateResource1RequestBody requestBody) {
        FunctionalResource functionalResource = new FunctionalResource(requestBody.id(), requestBody.attr1(), requestBody.attr2(), requestBody.attr3());
        functionalService.createResource1(functionalResource);

        return ResponseEntity.ok().build();
    }

    record CreateResource1RequestBody(@NotNull UUID id, @NotBlank String attr1, @Positive int attr2,
                                              @NotNull Date attr3) {
    }
}
