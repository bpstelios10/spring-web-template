package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.model.FunctionalResource;
import org.learnings.application_name.services.FunctionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class ETagEndpointTests {

    private static final String EXISTING_RESOURCE_ID1 = "f9734631-6833-4885-93c5-dd41679fc908";
    private static final String EXISTING_RESOURCE_ID2 = "f9734631-6833-4885-93c5-dd41679fc907";
    private static final String EXISTING_RESOURCE_ID3 = "f9734631-6833-4885-93c5-dd41679fc907";
    private static final String NON_EXISTING_RESOURCE_ID = "f9734631-6833-4885-93c5-dd41679fc900";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FunctionalService service;

    @Test
    void getResource1ByID_whenNoResourceWithThisIDAndWeakETagEnabled() throws Exception {
        mockMvc.perform(get("/etag/resource1/weak/" + NON_EXISTING_RESOURCE_ID))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("ETag"))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getResource1ByID_whenResourceExistsAndWeakETagEnabled() throws Exception {
        String eTagResponseHeader = mockMvc.perform(get("/etag/resource1/weak/" + EXISTING_RESOURCE_ID3))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXISTING_RESOURCE_ID3)))
                .andReturn().getResponse().getHeader("ETag");

        assertThat(eTagResponseHeader).isNotNull();

        mockMvc.perform(
                        get("/etag/resource1/weak/" + EXISTING_RESOURCE_ID3)
                                .ifNoneMatch(eTagResponseHeader)
                ).andExpect(status().isNotModified())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getResource1ByID_whenResourceExistsAndWeakETagEnabledAndResourceModified() throws Exception {
        String eTagResponseHeader = mockMvc.perform(get("/etag/resource1/weak/" + EXISTING_RESOURCE_ID1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXISTING_RESOURCE_ID1)))
                .andReturn().getResponse().getHeader("ETag");

        assertThat(eTagResponseHeader).isNotNull();

        // patch the existing resource to be different
        FunctionalResource updatedResource = new FunctionalResource(UUID.fromString(EXISTING_RESOURCE_ID1),
                "att1", 3, Date.from(Instant.parse("2024-02-01T08:16:24.00Z")));
        service.patchResource1(updatedResource);

        mockMvc.perform(
                        get("/etag/resource1/weak/" + EXISTING_RESOURCE_ID1)
                                .ifNoneMatch(eTagResponseHeader)
                ).andExpect(status().isOk())
                .andExpect(content().string(containsString(EXISTING_RESOURCE_ID1)))
                .andExpect(header().string("ETag", not(eTagResponseHeader))); // new eTag is different
    }

    @Test
    void getResource1ByID_whenNoResourceWithThisIDAndStrongETagEnabled() throws Exception {
        mockMvc.perform(get("/etag/resource1/strong/" + NON_EXISTING_RESOURCE_ID))
                .andExpect(status().isOk())
                // the spring filter computes some default ETag for the empty response body
                .andExpect(header().string("ETag", "\"0d41d8cd98f00b204e9800998ecf8427e\""))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getResource1ByID_whenResourceExistsAndStrongETagEnabled() throws Exception {
        String eTagResponseHeader = mockMvc.perform(get("/etag/resource1/strong/" + EXISTING_RESOURCE_ID3))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXISTING_RESOURCE_ID3)))
                .andReturn().getResponse().getHeader("ETag");

        assertThat(eTagResponseHeader).isNotNull();

        mockMvc.perform(
                        get("/etag/resource1/strong/" + EXISTING_RESOURCE_ID3)
                                .ifNoneMatch(eTagResponseHeader)
                ).andExpect(status().isNotModified())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getResource1ByID_whenResourceExistsAndStrongETagEnabledAndResourceModified() throws Exception {
        String eTagResponseHeader = mockMvc.perform(get("/etag/resource1/strong/" + EXISTING_RESOURCE_ID2))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXISTING_RESOURCE_ID2)))
                .andReturn().getResponse().getHeader("ETag");

        assertThat(eTagResponseHeader).isNotNull();

        // patch the existing resource to be different
        FunctionalResource updatedResource = new FunctionalResource(UUID.fromString(EXISTING_RESOURCE_ID2),
                "att1", 5, Date.from(Instant.parse("2024-02-03T08:16:24.00Z")));
        service.patchResource1(updatedResource);

        mockMvc.perform(
                        get("/etag/resource1/strong/" + EXISTING_RESOURCE_ID2)
                                .ifNoneMatch(eTagResponseHeader)
                ).andExpect(status().isOk())
                .andExpect(content().string(containsString(EXISTING_RESOURCE_ID2)))
                .andExpect(header().string("ETag", not(eTagResponseHeader))); // new eTag is different
    }
}
