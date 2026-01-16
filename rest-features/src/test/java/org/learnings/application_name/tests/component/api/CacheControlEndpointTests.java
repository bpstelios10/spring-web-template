package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class CacheControlEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getResource_withCachedMaxAge_thenReturnCacheHeader() throws Exception {
        String currentRequestCounter = mockMvc.perform(get("/cache-control/max-age/current-request"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "max-age=90, private"))
                .andReturn().getResponse().getContentAsString();

        // mockMvc does not cache the response so we cant verify the next lines
//        mockMvc.perform(get("/cache-control/max-age/current-request"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(is(currentRequestCounter)));
    }

    @Test
    void getResource_withNoCached_thenReturnNoCacheHeader() throws Exception {
        String currentRequestCounter = mockMvc.perform(get("/cache-control/no-cache/current-request"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "no-cache"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/cache-control/no-cache/current-request"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(currentRequestCounter)));
    }

    @Test
    void getResource_withFilterCached_thenReturnCacheHeader() throws Exception {
        String currentRequestCounter = mockMvc.perform(get("/cache-control/filter-cache-control/current-request"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate"))
                .andExpect(header().string(HttpHeaders.EXPIRES, "0"))
                .andExpect(header().string(HttpHeaders.PRAGMA, "no-cache"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/cache-control/filter-cache-control/current-request"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(currentRequestCounter)));
    }
}
