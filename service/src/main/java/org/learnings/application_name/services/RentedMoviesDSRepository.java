package org.learnings.application_name.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentedMoviesDSRepository {
    List<IRentedMoviesEntity> findByRentedMoviesEntityKeyClientID(UUID clientID);

    Optional<IRentedMoviesEntity> findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(UUID clientID, UUID movieID);

    IRentedMoviesEntity save(IRentedMoviesEntity rentedMovie);
}
