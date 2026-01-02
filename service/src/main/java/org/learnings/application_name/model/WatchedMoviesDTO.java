package org.learnings.application_name.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WatchedMoviesDTO {
    private final Set<WatchedRelationship> moviesWatched;

    public WatchedMoviesDTO(Set<WatchedRelationship> moviesWatched) {
        this.moviesWatched = moviesWatched;
    }

    public List<String> getMoviesWatched() {
        return moviesWatched.stream()
                .map(WatchedRelationship::getMovie)
                .map(Movie::getTitle)
                .toList();
    }

    public Map<String, Short> getMoviesAndRatingsWatched() {
        return moviesWatched.stream()
                .collect(Collectors.toMap(e -> e.getMovie().getTitle(), WatchedRelationship::getRating));
    }
}
