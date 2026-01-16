package org.learnings.application_name.services;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WatchedMoviesService {

    private final RentedMoviesDSRepository repository;
    private final IRentedMoviesEntityFactory rentedMovieEntityFactory;

    public WatchedMoviesService(RentedMoviesDSRepository repository, IRentedMoviesEntityFactory rentedMovieEntityFactory) {
        this.repository = repository;
        this.rentedMovieEntityFactory = rentedMovieEntityFactory;
    }

    public List<RentedMovieDTO> getAllRentedMoviesOfClient(UUID clientUUID) {
        return repository.findByRentedMoviesEntityKeyClientID(clientUUID).stream().map(RentedMovieDTO::fromRentedMovieEntity).toList();
    }

    public Optional<RentedMovieDTO> getRentedMovieForClient(UUID clientID, UUID movieID) {
        return repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientID, movieID).map(RentedMovieDTO::fromRentedMovieEntity);
    }

    public void addRentedMovieToClient(UUID clientID, UUID movieID) {
        Optional<IRentedMoviesEntity> findMovieForClientByID =
                repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientID, movieID);
        RentedMovieDTO toBeSaved = findMovieForClientByID
                .map(iRentedMoviesEntity -> new RentedMovieDTO(clientID, movieID, iRentedMoviesEntity.getTimesRented() + 1, iRentedMoviesEntity.getDateRented()))
                .orElseGet(() -> new RentedMovieDTO(clientID, movieID, 1, Date.from(Instant.now())));

        repository.save(rentedMovieEntityFactory.fromRentedMovieDTO(toBeSaved));
    }
}
