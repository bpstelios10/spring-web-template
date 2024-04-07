package org.learnings.application_name.infrastructure.repositories;

import org.learnings.application_name.model.Person;
import org.learnings.application_name.model.WatchedMoviesDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> findByName(String name);

    WatchedMoviesDTO findWatchedMoviesByName(String name);

    @Override
    @NonNull
    <S extends Person> S save(@NonNull S entity);
}
