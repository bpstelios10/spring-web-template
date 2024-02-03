package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.learnings.application_name.model.FunctionalResource;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionalServiceTest {

    private final FunctionalService service = new FunctionalService();
    private final FunctionalResource expectedResource =
            new FunctionalResource(
                    UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                    "att1",
                    3,
                    Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
            );

    @Test
    void getAllResource1() {
        List<FunctionalResource> allResource1 = service.getAllResource1();

        assertThat(allResource1).hasSize(3);
        assertThat(allResource1).contains(expectedResource);
    }

    @Test
    void getResource1ByID_whenResourceExists() {
        Optional<FunctionalResource> resource1ByID = service.getResource1ByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"));

        assertThat(resource1ByID).isNotEmpty();
        assertThat(resource1ByID.get()).isEqualTo(expectedResource);
    }

    @Test
    void getResource1ByID_whenResourceNotExists() {
        Optional<FunctionalResource> resource1ByID = service.getResource1ByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc909"));

        assertThat(resource1ByID).isEmpty();
    }

    @Test
    void createResource1_whenResourceNotExists() {
        service.createResource1(new FunctionalResource(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc904"),
                "att1",
                3,
                Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
        ));

        assertThat(service.getAllResource1()).hasSize(4);
    }

    @Test
    void createResource1_whenResourceExists() {
        service.createResource1(expectedResource);

        assertThat(service.getAllResource1()).hasSize(3);
    }
}
