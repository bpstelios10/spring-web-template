package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.learnings.application_name.model.FunctionalResource;

import java.time.Instant;
import java.util.Date;
import java.util.List;
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
}
