package org.learnings.application_name.services;

import org.learnings.application_name.model.RentedMovie;

import java.util.List;
import java.util.UUID;

public interface RentedMoviesDSRepository {
    List<RentedMovie> findByClientID(UUID clientUUID);

    RentedMovie findByClientIDAndMovieID(UUID clientUUID, UUID movieID);

    void save(RentedMovie rentedMovie);
}
