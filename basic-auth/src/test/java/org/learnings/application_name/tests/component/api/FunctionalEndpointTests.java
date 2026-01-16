package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class FunctionalEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("resource1-read-user")
    void getAllResource1_whenAuthenticatedUser_returnsResources() throws Exception {
        mockMvc.perform(get("/resource1"))
                .andExpect(status().isOk())
                .andExpect(content().string(stringContainsInOrder("f9734631-6833-4885-93c5-dd41679fc908", "f9734631-6833-4885-93c5-dd41679fc907", "f9734631-6833-4885-93c5-dd41679fc906")));
    }

    @Test
    void getAllResource1_whenNotAuthenticatedUser_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/resource1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("resource1-write-user")
    void getAllResource1_whenAuthenticatedButNotAuthorizedUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/resource1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "READ_RESOURCE1_ROLE")
    void getResource1ByID_whenAuthenticatedUserAndResourceExists_returnsResource() throws Exception {
        mockMvc.perform(get("/resource1/f9734631-6833-4885-93c5-dd41679fc908"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("f9734631-6833-4885-93c5-dd41679fc908")));
    }

    @Test
    @WithMockUser(username = "resource1-read-user", roles = "READ_RESOURCE1_ROLE")
    void getResource1ByID_whenAuthenticatedUserAndResourceNotExists_returnsEmpty() throws Exception {
        mockMvc.perform(get("/resource1/f9734631-6833-4885-93c5-dd41679fc900"))
                .andExpect(status().isOk())
                .andExpect(content().string(blankOrNullString()));
    }

    @Test
    void getResource1ByID_whenNotAuthenticatedUser_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/resource1/f9734631-6833-4885-93c5-dd41679fc900"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("resource1-write-user")
    void getResource1ByID_whenAuthenticatedButNotAuthorizedUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/resource1/f9734631-6833-4885-93c5-dd41679fc900"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"CREATE_RESOURCE1_ROLE", "READ_RESOURCE1_ROLE"})
    void createResource1_whenAuthenticatedUser_createsResources() throws Exception {
        UUID UUID1 = UUID.randomUUID();

        mockMvc.perform(post("/resource1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"" + UUID1 + "\",\"attr1\":\"att1\",\"attr2\":3,\"attr3\":\"2024-02-01 08:15\"}")
        ).andExpect(status().isOk()).andExpect(content().string(blankOrNullString()));

        mockMvc.perform(get("/resource1/" + UUID1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(UUID1.toString())));
    }

    @Test
    void createResource1_whenNotAuthenticatedUser_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/resource1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("resource1-read-user")
    void createResource1_whenAuthenticatedButNotAuthorizedUser_returnsForbidden() throws Exception {
        mockMvc.perform(post("/resource1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
        ).andExpect(status().isForbidden());
    }
}
