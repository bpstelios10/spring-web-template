package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchedMoviesServiceTest {

    @Mock
    private RentedMoviesDSRepository repository;
    @Mock
    private IRentedMoviesEntityFactory factory;
    @InjectMocks
    private WatchedMoviesService service;
    @Mock
    private IRentedMoviesEntity expectedRentedMovie;

    private static final UUID clientUUID = UUID.randomUUID();
    private final RentedMovieDTO expectedRentedMovieDTO = new RentedMovieDTO(clientUUID, UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
            3, Timestamp.from(Instant.parse("2024-02-01T08:15:24.00Z")));

    @Test
    void getAllRentedMoviesOfClient() {
        when(expectedRentedMovie.getClientID()).thenReturn(expectedRentedMovieDTO.clientID());
        when(expectedRentedMovie.getMovieID()).thenReturn(expectedRentedMovieDTO.movieID());
        when(expectedRentedMovie.getTimesRented()).thenReturn(expectedRentedMovieDTO.timesRented());
        when(expectedRentedMovie.getDateRented()).thenReturn(new Timestamp(expectedRentedMovieDTO.dateRented().getTime()));
        when(repository.findByRentedMoviesEntityKeyClientID(clientUUID)).thenReturn(List.of(expectedRentedMovie));

        List<RentedMovieDTO> allRentedMovies = service.getAllRentedMoviesOfClient(clientUUID);

        assertThat(allRentedMovies).hasSize(1);
        assertThat(allRentedMovies).contains(expectedRentedMovieDTO);
        verifyNoMoreInteractions(repository, expectedRentedMovie);
    }

    @Test
    void getRentedMovieForClient_whenRentedMovieExists() {
        when(expectedRentedMovie.getClientID()).thenReturn(expectedRentedMovieDTO.clientID());
        when(expectedRentedMovie.getMovieID()).thenReturn(expectedRentedMovieDTO.movieID());
        when(expectedRentedMovie.getTimesRented()).thenReturn(expectedRentedMovieDTO.timesRented());
        when(expectedRentedMovie.getDateRented()).thenReturn(new Timestamp(expectedRentedMovieDTO.dateRented().getTime()));
        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, expectedRentedMovie.getMovieID())).thenReturn(Optional.of(expectedRentedMovie));

        Optional<RentedMovieDTO> rentedMovieByID = service.getRentedMovieForClient(clientUUID, expectedRentedMovie.getMovieID());

        assertThat(rentedMovieByID).isNotEmpty();
        assertThat(rentedMovieByID.get()).isEqualTo(expectedRentedMovieDTO);
        verifyNoMoreInteractions(repository, expectedRentedMovie);
    }

    @Test
    void getRentedMovieForClient_whenRentedMovieNotExists() {
        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, expectedRentedMovieDTO.movieID())).thenReturn(Optional.empty());

        Optional<RentedMovieDTO> rentedMovieByID = service.getRentedMovieForClient(clientUUID, expectedRentedMovieDTO.movieID());

        assertThat(rentedMovieByID).isEmpty();
        verifyNoMoreInteractions(repository, expectedRentedMovie);
    }

    @Test
    void getAllRentedMoviesOfClient_whenRentedMovieNotExists() {
        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, expectedRentedMovieDTO.movieID())).thenReturn(Optional.empty());
        when(factory.fromRentedMovieDTO(expectedRentedMovieDTO)).thenReturn(expectedRentedMovie);
        when(repository.save(expectedRentedMovie)).thenReturn(expectedRentedMovie);

        service.addRentedMovieToClient(expectedRentedMovieDTO);

        verifyNoMoreInteractions(repository, expectedRentedMovie);
    }

    @Test
    void getAllRentedMoviesOfClient_whenRentedMovieExists() {
        when(repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, expectedRentedMovieDTO.movieID())).thenReturn(Optional.of(expectedRentedMovie));
        RentedMovieDTO oneMoreTimeRented = new RentedMovieDTO(expectedRentedMovieDTO.clientID(), expectedRentedMovieDTO.movieID(),
                expectedRentedMovieDTO.timesRented() + 1, expectedRentedMovieDTO.dateRented());
        when(factory.fromRentedMovieDTO(oneMoreTimeRented)).thenReturn(expectedRentedMovie);
        when(repository.save(expectedRentedMovie)).thenReturn(expectedRentedMovie);

        service.addRentedMovieToClient(expectedRentedMovieDTO);

        verifyNoMoreInteractions(repository);
    }
}
