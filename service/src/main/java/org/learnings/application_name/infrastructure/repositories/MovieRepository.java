package org.learnings.application_name.infrastructure.repositories;

import org.learnings.application_name.model.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    @Override
    @NonNull
    <S extends Movie> S save(@NonNull S entity);

    @Query("""
            MATCH (client:Person {name:$name})-[r:WATCHED]->(movie:Movie {title:$movieTitle})\s
            WITH r.rating as rating1, client\s
            MATCH (rec:Movie)<-[:WATCHED {rating:(rating1)}]-(a:Person)-[:WATCHED {rating:(rating1)}]->(m:Movie {title:$movieTitle})\s
            WHERE a<>client RETURN DISTINCT rec;""")
    List<Movie> getMoviesRatedSimilarlyByOtherViewers(String movieTitle, @Param("name") String viewerName);
}
