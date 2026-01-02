package org.learnings.application_name.services;

import org.learnings.application_name.infrastructure.repositories.MovieRepository;
import org.learnings.application_name.model.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    public Optional<Movie> getMovieByTitle(String title) {
        return repository.findByTitle(title);
    }

    public void createMovie(Movie movie) {
        if (repository.findByTitle(movie.getTitle()).isEmpty()) {
            repository.save(movie);
        }
    }

    public List<Movie> getRecommendationsBySimilarlyRatedMovies(String movieTitle, String viewerName) {
        return repository.getMoviesRatedSimilarlyByOtherViewers(movieTitle, viewerName);
    }
}
