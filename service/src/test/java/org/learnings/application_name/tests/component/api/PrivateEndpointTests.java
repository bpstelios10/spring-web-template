package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test-actuator")
public class PrivateEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPrivateStatus() throws Exception {
        mockMvc.perform(get("/private/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void getActuatorLinks() throws Exception {
        mockMvc.perform(get("/application_name/private").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['_links']").isNotEmpty());
    }

    @Test
    void getActuatorMetrics() throws Exception {
        mockMvc.perform(get("/private/status"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/application_name/private/metrics"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("http_server_requests_seconds_count{error=\"none\",exception=\"none\",method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/private/status\"} ")));
    }

    @Test
    void getActuatorConfigProps() throws Exception {
        mockMvc.perform(get("/application_name/private/configprops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contexts.application.beans").isNotEmpty());
    }

    @Test
    void getActuatorEnv() throws Exception {
        mockMvc.perform(get("/application_name/private/env"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeProfiles").value("component-test-actuator"));
    }

    @Test
    void getActuatorHeapdump() throws Exception {
        mockMvc.perform(get("/application_name/private/heapdump"))
                .andExpect(status().isOk());
    }

    @Test
    void getActuatorHealth() throws Exception {
        mockMvc.perform(get("/application_name/private/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['status']").value("UP"))
                .andExpect(content().string(containsString("liveness")))
                .andExpect(content().string(containsString("readiness")));
    }

    @Test
    void getActuatorLivenessCheck() throws Exception {
        mockMvc.perform(get("/application_name/private/health/liveness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['status']").value("UP"));
    }

    @Test
    void getActuatorReadinessCheck() throws Exception {
        mockMvc.perform(get("/application_name/private/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['status']").value("UP"));
    }
}
