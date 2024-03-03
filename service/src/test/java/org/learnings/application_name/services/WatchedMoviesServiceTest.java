package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.model.RentedMovie;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchedMoviesServiceTest {

    @Mock
    private RentedMoviesDSRepository repository;
    @InjectMocks
    private WatchedMoviesService service;

    private static final UUID clientUUID = UUID.randomUUID();
    private final RentedMovie expectedRentedMovie = new RentedMovie(clientUUID, UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
            3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z")));
    private final RentedMovieDTO expectedRentedMovieDTO = RentedMovieDTO.fromRentedMovie(expectedRentedMovie);

    @Test
    void getAllRentedMoviesOfClient() {
        when(repository.findByClientID(clientUUID)).thenReturn(List.of(expectedRentedMovie));

        List<RentedMovieDTO> allRentedMovies = service.getAllRentedMoviesOfClient(clientUUID);

        assertThat(allRentedMovies).hasSize(1);
        assertThat(allRentedMovies).contains(expectedRentedMovieDTO);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getRentedMovieForClient_whenRentedMovieExists() {
        when(repository.findByClientIDAndMovieID(clientUUID, expectedRentedMovie.movieID())).thenReturn(expectedRentedMovie);

        Optional<RentedMovieDTO> rentedMovieByID = service.getRentedMovieForClient(clientUUID, expectedRentedMovie.movieID());

        assertThat(rentedMovieByID).isNotEmpty();
        assertThat(rentedMovieByID.get()).isEqualTo(expectedRentedMovieDTO);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getRentedMovieForClient_whenRentedMovieNotExists() {
        when(repository.findByClientIDAndMovieID(clientUUID, expectedRentedMovie.movieID())).thenReturn(null);

        Optional<RentedMovieDTO> rentedMovieByID = service.getRentedMovieForClient(clientUUID, expectedRentedMovie.movieID());

        assertThat(rentedMovieByID).isEmpty();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllRentedMoviesOfClient_whenRentedMovieNotExists() {
        when(repository.findByClientIDAndMovieID(clientUUID, expectedRentedMovie.movieID())).thenReturn(null);
        doNothing().when(repository).save(expectedRentedMovie);

        service.addRentedMovieToClient(expectedRentedMovieDTO);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllRentedMoviesOfClient_whenRentedMovieExists() {
        when(repository.findByClientIDAndMovieID(clientUUID, expectedRentedMovie.movieID())).thenReturn(expectedRentedMovie);
        RentedMovie oneMoreTimeRented = new RentedMovie(expectedRentedMovie.clientID(), expectedRentedMovie.movieID(),
                expectedRentedMovie.timesRented() + 1, expectedRentedMovie.dateRented());
        doNothing().when(repository).save(oneMoreTimeRented);

        service.addRentedMovieToClient(expectedRentedMovieDTO);

        verifyNoMoreInteractions(repository);
    }
}
