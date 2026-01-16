package org.learnings.application_name.tests.component.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.learnings.application_name.infrastructure.repositories.RentedMoviesEntity;
import org.learnings.application_name.infrastructure.repositories.RentedMoviesEntityKey;
import org.learnings.application_name.infrastructure.repositories.RentedMoviesRepository;
import org.learnings.application_name.services.IRentedMoviesEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({LocalCassandraWithDockerExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RentedMoviesEndpointTests {
    @SuppressWarnings("unused")
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("unused")
    @MockitoBean
    private RentedMoviesRepository repository;

    private static final UUID clientUUID = UUID.randomUUID();
    private static final UUID firstMovieUUID = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<IRentedMoviesEntity> dataSource = List.of(
            new RentedMoviesEntity(new RentedMoviesEntityKey(clientUUID, firstMovieUUID),
                    3, Timestamp.from(Instant.parse("2024-02-01T08:15:00.00Z"))),
            new RentedMoviesEntity(new RentedMoviesEntityKey(clientUUID, UUID.fromString("f9734631-6833-4885-93c5-dd41679fc907")),
                    2, Timestamp.from(Instant.now().minus(Duration.ofDays(30)))),
            new RentedMoviesEntity(new RentedMoviesEntityKey(clientUUID, UUID.fromString("f9734631-6833-4885-93c5-dd41679fc906")),
                    1, Timestamp.from(Instant.now().minus(Duration.ofDays(3))))
    );

    @Test
    void getAllRentedMoviesOfClient() throws Exception {
        when(repository.findByRentedMoviesEntityKeyClientID(clientUUID)).thenReturn(dataSource);

        mockMvc.perform(get("/account/" + clientUUID + "/watchlist/movies"))
                .andExpect(status().isOk())
                .andExpect(content().string(stringContainsInOrder("f9734631-6833-4885-93c5-dd41679fc908", "f9734631-6833-4885-93c5-dd41679fc907", "f9734631-6833-4885-93c5-dd41679fc906")));
    }

    @Test
    void getRentedMovieForClient_whenRentedMovieExists() throws Exception {
        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, firstMovieUUID)).thenReturn(Optional.of(dataSource.getFirst()));

        mockMvc.perform(get("/account/" + clientUUID + "/watchlist/movies/f9734631-6833-4885-93c5-dd41679fc908"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("f9734631-6833-4885-93c5-dd41679fc908")));
    }

    @Test
    void getRentedMovieForClient_whenNoRentedMovieWithThisID() throws Exception {
        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, firstMovieUUID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/account/" + clientUUID + "/watchlist/movies/f9734631-6833-4885-93c5-dd41679fc900"))
                .andExpect(status().isOk())
                .andExpect(content().string(blankOrNullString()));
    }

    @Test
    void addRentedMovieToClient() throws Exception {
        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, firstMovieUUID))
                .thenReturn(Optional.empty());
        when(repository.save(dataSource.getFirst())).thenReturn(dataSource.getFirst());
        RentedMovieRequestModel requestBody = new RentedMovieRequestModel(firstMovieUUID.toString());

        mockMvc.perform(post("/account/" + clientUUID + "/watchlist/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isOk()).andExpect(content().string(blankOrNullString()));
    }

    @ParameterizedTest(name = "Input {index}: {0}")
    @ValueSource(strings = {"  ", "\t", "\n"})
    @NullAndEmptySource
    void addRentedMovie_shouldFail_forInvalidValues(String content) throws Exception {
        RentedMovieRequestModel requestBody = new RentedMovieRequestModel(content);

        mockMvc.perform(post("/account/" + clientUUID + "/watchlist/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void givenWrongClientUUIDFormat_whenGetAllRentedMoviesForClient_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/account/" + "12341234" + "/watchlist/movies"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenWrongClientUUIDFormat_whenGetRentedMovieForClient_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/account/" + "123413" + "/watchlist/movies/f9734631-6833-4885-93c5-dd41679fc908"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenWrongMovieUUIDFormat_whenGetRentedMovieForClient_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/account/" + clientUUID + "/watchlist/movies/234213412"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenWrongClientUUIDFormat_whenAddRentedMovieToClient_thenReturnBadRequest() throws Exception {
        RentedMovieRequestModel requestBody = new RentedMovieRequestModel(firstMovieUUID.toString());

        mockMvc.perform(post("/account/12342314/watchlist/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void givenWrongMovieUUIDFormat_whenAddRentedMovieToClient_thenReturnBadRequest() throws Exception {
        RentedMovieRequestModel requestBody = new RentedMovieRequestModel("12342134");

        mockMvc.perform(post("/account/" + clientUUID + "/watchlist/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isBadRequest());
    }

    record RentedMovieRequestModel(String movieID) {
    }
}
