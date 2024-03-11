package org.learnings.application_name.infrastructure.repositories;

import org.learnings.application_name.services.IRentedMoviesEntity;
import org.learnings.application_name.services.RentedMoviesDSRepository;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RentedMoviesRepository extends CassandraRepository<RentedMoviesEntity, RentedMoviesEntityKey>, RentedMoviesDSRepository {
    List<IRentedMoviesEntity> findByRentedMoviesEntityKeyClientID(UUID clientID);

    Optional<IRentedMoviesEntity> findByRentedMoviesEntityKeyClientIDAndRentedMoviesEntityKeyMovieID(UUID clientID, UUID movieID);

    @Override
    IRentedMoviesEntity save(IRentedMoviesEntity rentedMovie);
}
