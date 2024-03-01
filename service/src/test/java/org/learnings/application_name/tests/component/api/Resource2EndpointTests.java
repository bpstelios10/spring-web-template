package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.repositories.Resource2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class Resource2EndpointTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Resource2Repository repository;

    @Test
    void getAllResource2() throws Exception {
        repository.deleteAll();
        mockMvc.perform(get("/resource2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['_embedded']['resource2Entities']").isEmpty());

        mockMvc.perform(post("/resource2").content("{\"id\":10,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/resource2").content("{\"id\":11,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/resource2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['_embedded']['resource2Entities']").isNotEmpty());
    }

    @Test
    void getResource2ByID() throws Exception {
        mockMvc.perform(post("/resource2").content("{\"id\":1,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/resource2/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$['attr1']").value("attribute 1"));
    }

    @Test
    void createResource2() throws Exception {
        mockMvc.perform(get("/resource2/2")).andExpect(status().isNotFound());
        mockMvc.perform(post("/resource2").content("{\"id\":2,\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost/resource2/2"));
        mockMvc.perform(get("/resource2/2")).andExpect(status().isOk())
                .andExpect(jsonPath("$['attr1']").value("attribute 1"))
                .andExpect(jsonPath("$['_links']['self']['href']").isString());
    }

    @Test
    void updateResource2() throws Exception {
        mockMvc.perform(get("/resource2/3")).andExpect(status().isNotFound());
        mockMvc.perform(put("/resource2/3").content("{\"attr1\":\"attribute 1\",\"attr2\":\"attribute 2\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/resource2/3")).andExpect(status().isOk())
                .andExpect(content().string(containsString("resource2/3\"")));
    }
}
