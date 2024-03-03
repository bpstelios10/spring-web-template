package org.learnings.application_name.services;

import org.learnings.application_name.model.RentedMovie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WatchedMoviesService {

    private final RentedMoviesDSRepository repository;

    public WatchedMoviesService(RentedMoviesDSRepository repository) {
        this.repository = repository;
    }

    public List<RentedMovieDTO> getAllRentedMoviesOfClient(UUID clientUUID) {
        return repository.findByClientID(clientUUID).stream().map(RentedMovieDTO::fromRentedMovie).toList();
    }

    public Optional<RentedMovieDTO> getRentedMovieForClient(UUID clientUUID, UUID movieID) {
        return Optional.ofNullable(repository.findByClientIDAndMovieID(clientUUID, movieID)).map(RentedMovieDTO::fromRentedMovie);
    }

    public void addRentedMovieToClient(RentedMovieDTO rentedMovieDTO) {
        if (repository.findByClientIDAndMovieID(rentedMovieDTO.clientID(), rentedMovieDTO.movieID()) == null)
            repository.save(RentedMovieDTO.toRentedMovie(rentedMovieDTO));
        else
            repository.save(new RentedMovie(rentedMovieDTO.clientID(), rentedMovieDTO.movieID(),
                    rentedMovieDTO.timesRented() + 1, rentedMovieDTO.dateRented()));
    }
}
