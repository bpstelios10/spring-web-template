package org.learnings.application_name.services;

import java.sql.Timestamp;
import java.util.UUID;

public interface IRentedMoviesEntity {
    UUID getClientID();

    UUID getMovieID();

    int getTimesRented();

    Timestamp getDateRented();
}
