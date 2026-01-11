package org.learnings.application_name.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.services.FunctionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("etag/resource1")
public class ETagController extends FunctionalController {

    private final FunctionalService functionalService;

    public ETagController(FunctionalService functionalService) {
        super(functionalService);
        this.functionalService = functionalService;
    }

    @GetMapping("/strong/{id}")
    public ResponseEntity<Resource1ResponseModel> getResource1ByIDWithStrongETag(@PathVariable String id) {
        log.debug("requested resource with id [{}], using strong eTag control", id);
        //add check to validate id is uuid
        UUID requestID = UUID.fromString(id);

        return ResponseEntity.ok(
                functionalService.getResource1ByID(requestID)
                        .map(Resource1ResponseModel::fromDomainObject)
                        .orElse(null));
    }

    @GetMapping("/weak/{id}")
    public ResponseEntity<Resource1ResponseModel> getResource1ByIDWithWeakETag(@PathVariable String id) {
        log.debug("requested resource with id [{}], using weak (optimistic) eTag control, of 'last-modified' check", id);
        //add check to validate id is uuid
        UUID requestID = UUID.fromString(id);
        Resource1ResponseModel response = functionalService.getResource1ByID(requestID)
                .map(Resource1ResponseModel::fromDomainObject)
                .orElse(null);
        String eTag = response != null ? Long.toString(response.attr3().getTime()) : null;

        return ResponseEntity.ok()
                .eTag(eTag)
                .body(response);
    }
}
