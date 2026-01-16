package org.learnings.application_name.infrastructure.repositories;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.learnings.application_name.services.IRentedMoviesEntity;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Table(value = "rented_movies")
public class RentedMoviesEntity implements IRentedMoviesEntity {

    @PrimaryKey
    private RentedMoviesEntityKey rentedMoviesEntityKey;
    @Column("times_rented")
    private int timesRented;
    @Column("date_rented")
    private Timestamp dateRented;

    @Override
    public UUID getClientID() {
        return rentedMoviesEntityKey.getClientID();
    }

    @Override
    public UUID getMovieID() {
        return rentedMoviesEntityKey.getMovieID();
    }

    @Override
    public int getTimesRented() {
        return timesRented;
    }

    @Override
    public Timestamp getDateRented() {
        return dateRented;
    }
}

//@Table(value = "rented_movies")
//public record RentedMoviesEntity(
//        @PrimaryKeyColumn(name = "client_ID", ordinal = 0, type = PrimaryKeyType.PARTITIONED) UUID clientID,
//        @PrimaryKeyColumn(name = "movie_ID", ordinal = 1, type = PrimaryKeyType.CLUSTERED) UUID movieID,
//        @Column("times_rented") int timesRented,
//        @Column("date_rented") Timestamp dateRented) {
//
//}
