package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.repositories.Resource1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @Autowired
    private Resource1Repository repository;

    @Test
    void getAllResource1() throws Exception {
        repository.deleteAll();
        mockMvc.perform(get("/resource1Entities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['_embedded']['resource1Entities']").isEmpty());

        mockMvc.perform(post("/resource1Entities").content("{\"id\":10,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/resource1Entities").content("{\"id\":11,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/resource1Entities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['_embedded']['resource1Entities']").isNotEmpty());
    }

    @Test
    void getResource1ByID() throws Exception {
        mockMvc.perform(post("/resource1Entities").content("{\"id\":1,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/resource1Entities/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$['attr1']").value("attribute 1"));
    }

    @Test
    void createResource1() throws Exception {
        mockMvc.perform(get("/resource1Entities/2")).andExpect(status().isNotFound());
        mockMvc.perform(post("/resource1Entities").content("{\"id\":2,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost/resource1Entities/2"));
        mockMvc.perform(get("/resource1Entities/2")).andExpect(status().isOk())
                .andExpect(jsonPath("$['attr1']").value("attribute 1"))
                .andExpect(jsonPath("$['_links']['self']['href']").isString());
    }

    @Test
    void updateResource1() throws Exception {
        mockMvc.perform(get("/resource1Entities/3")).andExpect(status().isNotFound());
        mockMvc.perform(put("/resource1Entities/3").content("{\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/resource1Entities/3")).andExpect(status().isOk())
                .andExpect(content().string(containsString("resource1Entities/3\"")));
    }
}
