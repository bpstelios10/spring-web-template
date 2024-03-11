package org.learnings.application_name.services;

import org.learnings.application_name.model.RentedMovie;

import java.util.Date;
import java.util.UUID;

public record RentedMovieDTO(UUID clientID, UUID movieID, int timesRented, Date dateRented) implements RentedMovie {
    public static RentedMovieDTO fromRentedMovieEntity(IRentedMoviesEntity rentedMovieEntity) {
        return new RentedMovieDTO(rentedMovieEntity.getClientID(), rentedMovieEntity.getMovieID(),
                rentedMovieEntity.getTimesRented(), rentedMovieEntity.getDateRented());
    }
}
