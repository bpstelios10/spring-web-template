package org.learnings.application_name.infrastructure.repositories;

import org.learnings.application_name.services.IRentedMoviesEntityFactory;
import org.learnings.application_name.services.RentedMovieDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
public class RentedMoviesEntityFactory implements IRentedMoviesEntityFactory {
    @Override
    public RentedMoviesEntity fromRentedMovieDTO(RentedMovieDTO rentedMovieDTO) {
        return new RentedMoviesEntity(new RentedMoviesEntityKey(rentedMovieDTO.clientID(), rentedMovieDTO.movieID()),
                rentedMovieDTO.timesRented(), new Timestamp(rentedMovieDTO.dateRented().getTime()));
    }
}
