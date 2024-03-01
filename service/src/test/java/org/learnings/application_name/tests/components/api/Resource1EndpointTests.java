package org.learnings.application_name.tests.components.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing spring-data-rest created endpoint
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class Resource1EndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllResource1() throws Exception {
        mockMvc.perform(get("/resource1Entities")).andExpect(status().isOk());
    }

    @Test
    void getResource1ByID() throws Exception {
        mockMvc.perform(post("/resource1Entities").content("{\"id\":1,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/resource1Entities/1")).andExpect(status().isOk());
    }

    @Test
    void createResource1() throws Exception {
        mockMvc.perform(get("/resource1Entities/2")).andExpect(status().isNotFound());
        mockMvc.perform(post("/resource1Entities").content("{\"id\":2,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/resource1Entities/2")).andExpect(status().isOk());
    }

    @Test
    void updateResource1() throws Exception {
        mockMvc.perform(get("/resource1Entities/3")).andExpect(status().isNotFound());
        mockMvc.perform(put("/resource1Entities/3").content("{\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/resource1Entities/3")).andExpect(status().isOk());
    }
}
