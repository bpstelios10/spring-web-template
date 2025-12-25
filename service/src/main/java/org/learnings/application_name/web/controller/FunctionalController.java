package org.learnings.application_name.web.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    public ResponseEntity<List<Resource1ResponseModel>> getAllResource1() {
        log.debug("requested all resources");

        return ResponseEntity.ok(
                functionalService.getAllResource1()
                        .stream()
                        .map(Resource1ResponseModel::fromDomainObject)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource1ResponseModel> getResource1ByID(@PathVariable String id) {
        log.debug("requested resource with id [{}]", id);
        //add check to validate id is uuid
        UUID requestID = UUID.fromString(id);

        return ResponseEntity.ok(
                functionalService.getResource1ByID(requestID)
                        .map(Resource1ResponseModel::fromDomainObject)
                        .orElse(null));
    }

    @PostMapping
    public ResponseEntity<Void> createResource1(@Valid @RequestBody FunctionalController.Resource1RequestModel requestBody) {
        functionalService.createResource1(requestBody.toDomainObject());

        return ResponseEntity.ok().build();
    }

    record Resource1RequestModel(@NotNull UUID id, @NotBlank String attr1, @Positive int attr2,
                                 @NotNull @JsonFormat(pattern = "dd-MM-yyyy HH:mm") Date attr3) {
        FunctionalResource toDomainObject() {
            return new FunctionalResource(id, attr1, attr2, attr3);
        }
    }

    record Resource1ResponseModel(@NotNull UUID id, @NotBlank String attr1, @Positive int attr2,
                                  @NotNull @JsonFormat(pattern = "dd-MM-yyyy HH:mm") Date attr3) {
        static Resource1ResponseModel fromDomainObject(FunctionalResource functionalResource) {
            return new Resource1ResponseModel(functionalResource.id(), functionalResource.attribute1(), functionalResource.attribute2(),
                    functionalResource.attribute3());
        }
    }
}
