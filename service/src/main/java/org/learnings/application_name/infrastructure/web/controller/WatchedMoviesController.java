package org.learnings.application_name.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.services.RentedMovieDTO;
import org.learnings.application_name.services.WatchedMoviesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("account/{clientID}/watchlist/movies")
public class WatchedMoviesController {

    private final WatchedMoviesService watchedMoviesService;

    public WatchedMoviesController(WatchedMoviesService watchedMoviesService) {
        this.watchedMoviesService = watchedMoviesService;
    }

    @GetMapping
    public ResponseEntity<List<RentedMovieDTO>> getAllRentedMoviesOfClient(@PathVariable String clientID) {
        log.debug("requested all rented movies for clientID [{}]", clientID);
        UUID clientUUID = UUID.fromString(clientID);

        return ResponseEntity.ok(watchedMoviesService.getAllRentedMoviesOfClient(clientUUID));
    }

    @GetMapping("/{movieID}")
    public ResponseEntity<RentedMovieDTO> getRentedMovieForClient(@PathVariable String clientID, @PathVariable String movieID) {
        log.debug("requested movie with id [{}], for client with id [{}]", movieID, clientID);
        //add check to validate movieID is uuid
        UUID clientUUID = UUID.fromString(clientID);
        UUID movieUUID = UUID.fromString(movieID);

        return ResponseEntity.ok(watchedMoviesService.getRentedMovieForClient(clientUUID, movieUUID).orElse(null));
    }

    @PostMapping
    public ResponseEntity<Void> addRentedMovieToClient(@PathVariable String clientID,
                                                       @Valid @RequestBody WatchedMoviesController.AddRentedMovieRequestBody requestBody) {
        log.debug("add rented movie with id [{}], for clientID [{}]", requestBody.movieID(), clientID);

        UUID clientUUID = UUID.fromString(clientID);
        RentedMovieDTO rentedMovie = new RentedMovieDTO(clientUUID, requestBody.movieID(), requestBody.timesRented(), requestBody.dateRented());
        watchedMoviesService.addRentedMovieToClient(rentedMovie);

        return ResponseEntity.ok().build();
    }

    record AddRentedMovieRequestBody(@NotNull UUID movieID, @Positive int timesRented,
                                     @NotNull @JsonFormat(pattern = "dd-MM-yyyy HH:mm") Date dateRented) {
    }
}
