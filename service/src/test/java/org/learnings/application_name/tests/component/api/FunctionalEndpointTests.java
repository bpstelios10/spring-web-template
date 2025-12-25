package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class FunctionalEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllResource1() throws Exception {
        mockMvc.perform(get("/resource1"))
                .andExpect(status().isOk())
                .andExpect(content().string(stringContainsInOrder("f9734631-6833-4885-93c5-dd41679fc908", "f9734631-6833-4885-93c5-dd41679fc907", "f9734631-6833-4885-93c5-dd41679fc906")));
    }

    @Test
    void getResource1ByID_whenResourceExists() throws Exception {
        mockMvc.perform(get("/resource1/f9734631-6833-4885-93c5-dd41679fc908"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("f9734631-6833-4885-93c5-dd41679fc908")));
    }

    @Test
    void getResource1ByID_whenNoResourceWithThisID() throws Exception {
        mockMvc.perform(get("/resource1/f9734631-6833-4885-93c5-dd41679fc900"))
                .andExpect(status().isOk())
                .andExpect(content().string(blankOrNullString()));
    }

    @Test
    void createResource1() throws Exception {
        UUID UUID1 = UUID.randomUUID();
        mockMvc.perform(post("/resource1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"" + UUID1 + "\",\"attr1\":\"att1\",\"attr2\":3,\"attr3\":\"2024-02-01 08:15\"}")
        ).andExpect(status().isOk()).andExpect(content().string(blankOrNullString()));
        mockMvc.perform(get("/resource1/" + UUID1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(UUID1.toString())));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValuesForMessageBody")
    void createResource1_shouldFail_forBlankValues(String content) throws Exception {
        mockMvc.perform(post("/resource1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidValuesForMessageBody() {
        UUID UUID1 = UUID.randomUUID();
        return Stream.of(
                // null values
                Arguments.of("{\"attr1\":\"att1\",\"attr2\":3,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr2\":3,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"att1\",\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"att1\",\"attr2\":3}"),
                // blank strings
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"\",\"attr2\":3,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\" \",\"attr2\":3,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"\t\",\"attr2\":3,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"\n\",\"attr2\":3,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                // non positive int
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"att1\",\"attr2\":0,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"att1\",\"attr2\":-1,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}"),
                Arguments.of("{\"id\":\"" + UUID1 + "\",\"attr1\":\"att1\",\"attr2\":9999999999,\"attr3\":\"2024-02-01T08:15:24.000+00:00\"}")
        );
    }
}
