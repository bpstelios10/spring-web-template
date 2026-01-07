package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CachedResourcesEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCachedEndpoint1_returnsCachedCounter() throws Exception {
        String counter1 = mockMvc.perform(get("/cached/without-evict"))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue(String.class)))
                .andReturn().getResponse().getContentAsString();

        // assuming no race conditions, cause no other tests use this endpoint right now
        mockMvc.perform(get("/cached/without-evict"))
                .andExpect(status().isOk())
                .andExpect(content().string(is(counter1)));
    }

    @Test
    void getCachedEndpoint2_returnsCachedCounter_andThenEvicted() throws Exception {
        String counter1 = mockMvc.perform(get("/cached/with-evict"))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue(String.class)))
                .andReturn().getResponse().getContentAsString();

        // assuming no race conditions, cause no other tests use this endpoint right now
        mockMvc.perform(get("/cached/with-evict"))
                .andExpect(status().isOk())
                .andExpect(content().string(is(counter1)));

        mockMvc.perform(delete("/cached/resource2/cache"))
                .andExpect(status().isAccepted());

        // to avoid race conditions with cache evict
        given().ignoreException(AssertionError.class)
                .await()
                .atMost(600, TimeUnit.MILLISECONDS)
                .with()
                .pollInterval(250, TimeUnit.MILLISECONDS)
                .until(() -> {
                    System.out.println("****** inside awaitility attempts block");
                    mockMvc.perform(get("/cached/with-evict"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(not(counter1)));
                    return true;
                });
    }

    @Test
    void getCachedEndpoint3_returnsNonCachedCounter() throws Exception {
        String counter1 = mockMvc.perform(get("/cached/no-cached"))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue(String.class)))
                .andReturn().getResponse().getContentAsString();

        // assuming no race conditions, cause no other tests use this endpoint right now
        mockMvc.perform(get("/cached/no-cached"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(counter1)));
    }
}
