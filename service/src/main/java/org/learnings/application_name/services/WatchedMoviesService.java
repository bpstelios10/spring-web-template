package org.learnings.application_name.services;

import org.springframework.stereotype.Component;

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

    public Optional<RentedMovieDTO> getRentedMovieForClient(UUID clientUUID, UUID movieID) {
        return repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(clientUUID, movieID).map(RentedMovieDTO::fromRentedMovieEntity);
    }

    public void addRentedMovieToClient(RentedMovieDTO rentedMovieDTO) {
        RentedMovieDTO toBeSaved;
        if (repository.findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(rentedMovieDTO.clientID(), rentedMovieDTO.movieID()).isEmpty())
            toBeSaved = rentedMovieDTO;
        else
            toBeSaved = new RentedMovieDTO(rentedMovieDTO.clientID(), rentedMovieDTO.movieID(),
                    rentedMovieDTO.timesRented() + 1, rentedMovieDTO.dateRented());

        repository.save(rentedMovieEntityFactory.fromRentedMovieDTO(toBeSaved));
    }
}
