package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.learnings.application_name.infrastructure.repositories.RentedMoviesEntity;
import org.learnings.application_name.infrastructure.repositories.RentedMoviesEntityKey;
import org.learnings.application_name.infrastructure.repositories.RentedMoviesRepository;
import org.learnings.application_name.services.IRentedMoviesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing spring-web and spring-actuator endpoints
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class RentedMoviesEndpointTests {
    // TODO fix tests

//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private RentedMoviesRepository repository;
//
//    private static final UUID clientUUID = UUID.randomUUID();
//    private static final UUID firstMovieUUID = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908");
//
//    private final List<IRentedMoviesEntity> dataSource = List.of(
//            new RentedMoviesEntity(new RentedMoviesEntityKey(clientUUID, firstMovieUUID),
//                    3, Timestamp.from(Instant.parse("2024-02-01T08:15:24.00Z"))),
//            new RentedMoviesEntity(new RentedMoviesEntityKey(clientUUID, UUID.fromString("f9734631-6833-4885-93c5-dd41679fc907")),
//                    2, Timestamp.from(Instant.now().minus(Duration.ofDays(30)))),
//            new RentedMoviesEntity(new RentedMoviesEntityKey(clientUUID, UUID.fromString("f9734631-6833-4885-93c5-dd41679fc906")),
//                    1, Timestamp.from(Instant.now().minus(Duration.ofDays(3))))
//    );
//
//    @Test
//    void getAllRentedMoviesOfClient() throws Exception {
//        when(repository.findByRentedMoviesEntityKeyClientID(clientUUID)).thenReturn(dataSource);
//
//        mockMvc.perform(get("/account/" + clientUUID + "/watchlist/movies"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(stringContainsInOrder("f9734631-6833-4885-93c5-dd41679fc908", "f9734631-6833-4885-93c5-dd41679fc907", "f9734631-6833-4885-93c5-dd41679fc906")));
//    }
//
//    @Test
//    void getRentedMovieForClient_whenRentedMovieExists() throws Exception {
//        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, firstMovieUUID)).thenReturn(Optional.of(dataSource.get(0)));
//
//        mockMvc.perform(get("/account/" + clientUUID + "/watchlist/movies/f9734631-6833-4885-93c5-dd41679fc908"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("f9734631-6833-4885-93c5-dd41679fc908")));
//    }
//
//    @Test
//    void getRentedMovieForClient_whenNoRentedMovieWithThisID() throws Exception {
//        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, firstMovieUUID)).thenReturn(null);
//
//        mockMvc.perform(get("/account/" + clientUUID + "/watchlist/movies/f9734631-6833-4885-93c5-dd41679fc900"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(blankOrNullString()));
//    }
//
//    @Test
//    void addRentedMovieToClient() throws Exception {
//        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, firstMovieUUID)).thenReturn(null);
//        doNothing().when(repository).save(dataSource.get(0));
//
//        mockMvc.perform(post("/account/" + clientUUID + "/watchlist/movies")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"movieID\":\"" + firstMovieUUID + "\",\"timesRented\":3,\"dateRented\":\"2024-02-01T08:15:24.000+00:00\"}")
//        ).andExpect(status().isOk()).andExpect(content().string(blankOrNullString()));
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideInvalidValuesForMessageBody")
//    void addRentedMovie_shouldFail_forBlankValues(String content) throws Exception {
//        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, firstMovieUUID)).thenReturn(null);
//        doNothing().when(repository).save(dataSource.get(0));
//
//        mockMvc.perform(post("/account/" + clientUUID + "/watchlist/movies")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        ).andExpect(status().isBadRequest());
//    }
//
//    private static Stream<Arguments> provideInvalidValuesForMessageBody() {
//        UUID UUID1 = UUID.randomUUID();
//        return Stream.of(
//                // null values
//                Arguments.of("{\"timesRented\":3,\"dateRented\":\"2024-02-01T08:15:24.000+00:00\"}"),
//                Arguments.of("{\"movieID\":\"" + UUID1 + "\",\"dateRented\":\"2024-02-01T08:15:24.000+00:00\"}"),
//                Arguments.of("{\"movieID\":\"" + UUID1 + "\",\"timesRented\":3}"),
//                // non positive int
//                Arguments.of("{\"movieID\":\"" + UUID1 + "\",\"timesRented\":0,\"dateRented\":\"2024-02-01T08:15:24.000+00:00\"}"),
//                Arguments.of("{\"movieID\":\"" + UUID1 + "\",\"timesRented\":-1,\"dateRented\":\"2024-02-01T08:15:24.000+00:00\"}"),
//                Arguments.of("{\"movieID\":\"" + UUID1 + "\",\"timesRented\":9999999999,\"dateRented\":\"2024-02-01T08:15:24.000+00:00\"}")
//        );
//    }
}
