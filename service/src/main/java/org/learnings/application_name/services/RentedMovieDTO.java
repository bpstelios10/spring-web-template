package org.learnings.application_name.services;

import org.learnings.application_name.model.RentedMovie;

import java.util.Date;
import java.util.UUID;

public record RentedMovieDTO(UUID clientID, UUID movieID, int timesRented, Date dateRented) {
    public static RentedMovieDTO fromRentedMovie(RentedMovie rentedMovie) {
        return new RentedMovieDTO(rentedMovie.clientID(), rentedMovie.movieID(), rentedMovie.timesRented(), rentedMovie.dateRented());
    }

    public static RentedMovie toRentedMovie(RentedMovieDTO rentedMovieDTO) {
        return new RentedMovie(rentedMovieDTO.clientID(), rentedMovieDTO.movieID(), rentedMovieDTO.timesRented(), rentedMovieDTO.dateRented());
    }
}
