package org.learnings.application_name.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    public ResponseEntity<List<RentedMovieResponseModel>> getAllRentedMoviesOfClient(@PathVariable String clientID) {
        log.debug("requested all rented movies for clientID [{}]", clientID);

        UUID clientUUID = getUUID(clientID);
        List<RentedMovieResponseModel> allRentedMoviesOfClient =
                watchedMoviesService.getAllRentedMoviesOfClient(clientUUID)
                        .stream()
                        .map(RentedMovieResponseModel::fromDTO)
                        .toList();

        return ResponseEntity.ok(allRentedMoviesOfClient);
    }

    @GetMapping("/{movieID}")
    public ResponseEntity<RentedMovieResponseModel> getRentedMovieForClient(@PathVariable String clientID, @PathVariable String movieID) {
        log.debug("requested movie with id [{}], for client with id [{}]", movieID, clientID);

        UUID clientUUID = getUUID(clientID);
        UUID movieUUID = getUUID(movieID);
        RentedMovieResponseModel rentedMovieForClient =
                watchedMoviesService.getRentedMovieForClient(clientUUID, movieUUID)
                        .map(RentedMovieResponseModel::fromDTO)
                        .orElse(null);

        return ResponseEntity.ok(rentedMovieForClient);
    }

    @PostMapping
    public ResponseEntity<Void> addRentedMovieToClient(@PathVariable String clientID, @Valid @RequestBody RentedMovieRequestModel requestBody) {
        log.debug("add rented movie with id [{}], for clientID [{}]", requestBody.movieID(), clientID);

        UUID clientUUID = getUUID(clientID);
        UUID movieUUID = getUUID(requestBody.movieID());
        watchedMoviesService.addRentedMovieToClient(clientUUID, movieUUID);

        return ResponseEntity.ok().build();
    }

    public record RentedMovieRequestModel(@NotBlank String movieID) {
    }

    public record RentedMovieResponseModel(@NotNull UUID clientID,
                                    @NotNull UUID movieID,
                                    @Positive int timesRented,
                                    @NotNull @JsonFormat(pattern = "dd-MM-yyyy HH:mm") Date dateRented) {
        static RentedMovieResponseModel fromDTO(RentedMovieDTO rentedMovieDTO) {
            return new RentedMovieResponseModel(rentedMovieDTO.clientID(), rentedMovieDTO.movieID(), rentedMovieDTO.timesRented(),
                    rentedMovieDTO.dateRented());
        }
    }

    private UUID getUUID(String stringUUID) {
        try {
            return UUID.fromString(stringUUID);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(stringUUID);
        }
    }
}
