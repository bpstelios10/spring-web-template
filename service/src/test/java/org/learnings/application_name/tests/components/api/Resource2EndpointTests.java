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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class Resource2EndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllResource2() throws Exception {
        mockMvc.perform(get("/resource2")).andExpect(status().isOk());
    }

    @Test
    void getResource2ByID() throws Exception {
        mockMvc.perform(post("/resource2").content("{\"id\":1,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/resource2/1")).andExpect(status().isOk());
    }

    @Test
    void createResource2() throws Exception {
        mockMvc.perform(get("/resource2/2")).andExpect(status().isNotFound());
        mockMvc.perform(post("/resource2").content("{\"id\":2,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/resource2/2")).andExpect(status().isOk());
    }

    @Test
    void updateResource2() throws Exception {
        mockMvc.perform(get("/resource2/3")).andExpect(status().isNotFound());
        mockMvc.perform(put("/resource2/3").content("{\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/resource2/3")).andExpect(status().isOk());
    }
}
