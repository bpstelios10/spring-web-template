package org.learnings.application_name.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.services.RentedMovieDTO;
import org.learnings.application_name.services.WatchedMoviesService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class WatchedMoviesControllerTest {

    @Mock
    private WatchedMoviesService service;
    @InjectMocks
    private WatchedMoviesController controller;
    private static final UUID clientUUID = UUID.randomUUID();
    private static final UUID firstMovieUUID = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908");

    private final List<RentedMovieDTO> dataSource = List.of(
            new RentedMovieDTO(clientUUID, firstMovieUUID, 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))),
            new RentedMovieDTO(clientUUID, UUID.randomUUID(), 5, Date.from(Instant.now().minus(Duration.ofDays(2)))),
            new RentedMovieDTO(clientUUID, UUID.randomUUID(), 1, Date.from(Instant.now().minus(Duration.ofDays(30))))
    );

    @Test
    void getAllRentedMoviesOfClient_whenRentedMoviesExist() {
        when(service.getAllRentedMoviesOfClient(clientUUID)).thenReturn(dataSource);
        WatchedMoviesController.RentedMovieResponseModel expectedRentedMovie =
                new WatchedMoviesController.RentedMovieResponseModel(clientUUID, firstMovieUUID, 3,
                        Date.from(Instant.parse("2024-02-01T08:15:24.00Z")));

        ResponseEntity<List<WatchedMoviesController.RentedMovieResponseModel>> response = controller.getAllRentedMoviesOfClient(clientUUID.toString());

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody()).contains(expectedRentedMovie);
    }

    @Test
    void getAllRentedMoviesOfClient_whenRentedMoviesEmpty() {
        when(service.getAllRentedMoviesOfClient(clientUUID)).thenReturn(List.of());

        ResponseEntity<List<WatchedMoviesController.RentedMovieResponseModel>> response = controller.getAllRentedMoviesOfClient(clientUUID.toString());

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(0);
    }

    @Test
    void getRentedMovieForClient_whenRentedMoviesExist() {
        UUID clientID = UUID.randomUUID();
        WatchedMoviesController.RentedMovieResponseModel expectedRentedMoviesResponse =
                new WatchedMoviesController.RentedMovieResponseModel(clientID, firstMovieUUID, 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z")));
        RentedMovieDTO expectedRentedMovies =
                new RentedMovieDTO(clientID, firstMovieUUID, 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z")));
        when(service.getRentedMovieForClient(clientUUID, firstMovieUUID)).thenReturn(Optional.of(expectedRentedMovies));

        ResponseEntity<WatchedMoviesController.RentedMovieResponseModel> response =
                controller.getRentedMovieForClient(clientUUID.toString(), "f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedRentedMoviesResponse);
    }

    @Test
    void getRentedMovieForClient_whenRentedMoviesEmpty() {
        when(service.getRentedMovieForClient(clientUUID, firstMovieUUID)).thenReturn(Optional.empty());

        ResponseEntity<WatchedMoviesController.RentedMovieResponseModel> response =
                controller.getRentedMovieForClient(clientUUID.toString(), "f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void addRentedMovieToClient() {
        doNothing().when(service).addRentedMovieToClient(clientUUID, firstMovieUUID);
        WatchedMoviesController.RentedMovieRequestModel requestBody =
                new WatchedMoviesController.RentedMovieRequestModel(firstMovieUUID.toString());

        ResponseEntity<Void> response = controller.addRentedMovieToClient(clientUUID.toString(), requestBody);

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
