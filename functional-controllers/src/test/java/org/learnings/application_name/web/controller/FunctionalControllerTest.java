package org.learnings.application_name.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.model.FunctionalResource;
import org.learnings.application_name.services.FunctionalService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class FunctionalControllerTest {

    @Mock
    private FunctionalService service;
    @InjectMocks
    private FunctionalController controller;

    private final List<FunctionalResource> dataSource = List.of(
            new FunctionalResource(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"), "att1", 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))),
            new FunctionalResource(UUID.randomUUID(), "att2", 5, Date.from(Instant.now().minus(Duration.ofDays(2)))),
            new FunctionalResource(UUID.randomUUID(), "att3", 1, Date.from(Instant.now().minus(Duration.ofDays(30))))
    );

    @Test
    void getAllResource1_whenResourcesExist() {
        when(service.getAllResource1()).thenReturn(dataSource);
        FunctionalController.Resource1ResponseModel expectedResource = new FunctionalController.Resource1ResponseModel(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "att1",
                3,
                Date.from(Instant.parse("2024-02-01T08:15:24.00Z")));

        ResponseEntity<List<FunctionalController.Resource1ResponseModel>> allResource1 = controller.getAllResource1();

        assertThat(allResource1.getStatusCode()).isEqualTo(OK);
        assertThat(allResource1.getBody()).hasSize(3);
        assertThat(allResource1.getBody()).contains(expectedResource);
    }

    @Test
    void getAllResource1_whenResourcesEmpty() {
        when(service.getAllResource1()).thenReturn(List.of());

        ResponseEntity<List<FunctionalController.Resource1ResponseModel>> allResource1 = controller.getAllResource1();

        assertThat(allResource1.getStatusCode()).isEqualTo(OK);
        assertThat(allResource1.getBody()).hasSize(0);
    }

    @Test
    void getResource1ByID_whenResourcesExist() {
        FunctionalController.Resource1ResponseModel expectedResponseModel = new FunctionalController.Resource1ResponseModel(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "att1",
                3,
                Date.from(Instant.parse("2024-02-01T08:15:24.00Z")));
        FunctionalResource expectedResource = new FunctionalResource(expectedResponseModel.id(),
                expectedResponseModel.attr1(), expectedResponseModel.attr2(), expectedResponseModel.attr3());
        when(service.getResource1ByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"))).thenReturn(Optional.of(expectedResource));

        ResponseEntity<FunctionalController.Resource1ResponseModel> allResource1 = controller.getResource1ByID("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(allResource1.getStatusCode()).isEqualTo(OK);
        assertThat(allResource1.getBody()).isEqualTo(expectedResponseModel);
    }

    @Test
    void getResource1ByID_whenResourcesEmpty() {
        when(service.getResource1ByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"))).thenReturn(Optional.empty());

        ResponseEntity<FunctionalController.Resource1ResponseModel> allResource1 = controller.getResource1ByID("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(allResource1.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void createResource1() {
        FunctionalResource expectedResource = new FunctionalResource(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "att1",
                3,
                Date.from(Instant.parse("2024-02-01T08:15:24.00Z")));
        FunctionalController.Resource1RequestModel requestBody = new FunctionalController.Resource1RequestModel(
                expectedResource.id(), expectedResource.attribute1(), expectedResource.attribute2(), expectedResource.attribute3());
        doNothing().when(service).createResource1(expectedResource);

        ResponseEntity<Void> allResource1 = controller.createResource1(requestBody);

        assertThat(allResource1.getStatusCode()).isEqualTo(OK);
    }
}
