package org.learnings.application_name.web.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.model.Movie;
import org.learnings.application_name.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("recommendations")
public class RecommendationsController {

    private final MovieService movieService;

    public RecommendationsController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/person/{name}/movie/{title}")
    public ResponseEntity<List<MovieResponseModel>> getRecommendationsBySimilarlyRatedMovies(
            @PathVariable @NotBlank String name, @PathVariable @NotBlank String title) {
        log.debug("requested recommendations for user: [{}], similar to movie: [{}]", name, title);

        return ResponseEntity.ok(movieService.getRecommendationsBySimilarlyRatedMovies(title, name)
                .stream()
                .map(MovieResponseModel::fromDomainObject)
                .toList());
    }

    record MovieResponseModel(@NotNull Long id, @NotBlank String title, @NotBlank String description) {

        static MovieResponseModel fromDomainObject(Movie movie) {
            return new MovieResponseModel(movie.getId(), movie.getTitle(), movie.getDescription());
        }
    }
}
