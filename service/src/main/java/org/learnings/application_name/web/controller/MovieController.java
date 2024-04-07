package org.learnings.application_name.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.model.Movie;
import org.learnings.application_name.model.Person;
import org.learnings.application_name.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseModel>> getAllMovies() {
        log.debug("requested all movie");

        return ResponseEntity.ok(movieService.getAllMovies()
                .stream()
                .map(MovieResponseModel::fromDomainObject)
                .toList());
    }

    @GetMapping("/{title}")
    public ResponseEntity<MovieResponseModel> getMovieByTitle(@PathVariable @NotBlank String title) {
        log.debug("requested movie with title: [{}]", title);

        return ResponseEntity.ok(movieService.getMovieByTitle(title)
                .map(MovieResponseModel::fromDomainObject)
                .orElse(null));
    }

    @PostMapping
    public ResponseEntity<Void> createMovie(@Valid @RequestBody MovieController.MovieRequestModel requestBody) {
        movieService.createMovie(requestBody.toDomainObject());

        return ResponseEntity.ok()
                .build();
    }

    record MovieRequestModel(@NotBlank String title, @NotBlank String description) {

        Movie toDomainObject() {
            return new Movie(title, description);
        }
    }

    record MovieResponseModel(@NotNull Long id, @NotBlank String title, @NotBlank String description,
                              Set<Person> actors, Set<Person> directors) {

        static MovieResponseModel fromDomainObject(Movie movie) {
            return new MovieResponseModel(movie.getId(), movie.getTitle(), movie.getDescription(),
                    movie.getActors(), movie.getDirectors());
        }
    }
}
