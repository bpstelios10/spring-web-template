package org.learnings.application_name.services;

import org.learnings.application_name.model.FunctionalResource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
public class FunctionalService {

    //this would be better injected, but we don't want to add configuration files and complexity in the scope of this ex
    private static final Map<UUID, FunctionalResource> dataSource = new HashMap<>();

    static {
        UUID UUID1 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908");
        UUID UUID2 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc907");
        UUID UUID3 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc906");
        dataSource.put(UUID1, new FunctionalResource(UUID1, "att1", 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))));
        dataSource.put(UUID2, new FunctionalResource(UUID2, "att2", 5, Date.from(Instant.now().minus(Duration.ofDays(2)))));
        dataSource.put(UUID3, new FunctionalResource(UUID3, "att3", 1, Date.from(Instant.now().minus(Duration.ofDays(30)))));
    }

    public List<FunctionalResource> getAllResource1() {
        return callToDataSource();
    }

    public Optional<FunctionalResource> getResource1ByID(UUID requestID) {
        return Optional.ofNullable(dataSource.get(requestID));
    }

    public void createResource1(FunctionalResource expectedResource) {
        if (!dataSource.containsKey(expectedResource.id()))
            dataSource.put(expectedResource.id(), expectedResource);
    }

    private List<FunctionalResource> callToDataSource() {
        return dataSource.values().stream().toList();
    }
}
