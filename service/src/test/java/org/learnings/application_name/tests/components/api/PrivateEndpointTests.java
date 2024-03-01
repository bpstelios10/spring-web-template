package org.learnings.application_name.tests.components.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test-exported-metrics")
public class PrivateEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPrivateStatus() throws Exception {
        mockMvc.perform(get("/private/status")).andExpect(status().isOk());
    }

    @Test
    void getPrivateMetrics() throws Exception {
        mockMvc.perform(get("/private/metrics")).andExpect(status().isOk());
    }
}
