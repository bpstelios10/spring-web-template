package org.learnings.application_name.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.model.Movie;
import org.learnings.application_name.model.Person;
import org.learnings.application_name.model.WatchedRelationship;
import org.learnings.application_name.services.PersonService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService service;
    @InjectMocks
    private PersonController controller;

    private final Set<WatchedRelationship> watchedRelationships =
            Set.of(new WatchedRelationship(1L, new Movie("The Matrix", "desc"), (short) 2));
    private final List<Person> dataSource = List.of(
            new Person(1001L, "first one", 1989, watchedRelationships),
            new Person(1002L, "second one", 1999, new HashSet<>()),
            new Person(1003L, "third one", 1980, new HashSet<>())
    );

    @Test
    void getAllPersons_whenPersonsExist() {
        when(service.getAllPersons()).thenReturn(dataSource);
        PersonController.PersonResponseModel expectedPerson =
                new PersonController.PersonResponseModel(1001L, "first one", 1989,
                        List.of(dataSource.stream()
                                .filter(e -> e.getId() == 1001L)
                                .findFirst().get()
                                .getMoviesWatched().stream()
                                .filter(e -> e.getId() == 1L)
                                .findFirst().get()
                                .getMovie().getTitle())
                );

        ResponseEntity<List<PersonController.PersonResponseModel>> allPerson = controller.getAllPersons();

        assertThat(allPerson.getStatusCode()).isEqualTo(OK);
        assertThat(allPerson.getBody()).hasSize(3);
        assertThat(allPerson.getBody()).contains(expectedPerson);
    }

    @Test
    void getAllPersons_whenPersonsEmpty() {
        when(service.getAllPersons()).thenReturn(List.of());

        ResponseEntity<List<PersonController.PersonResponseModel>> allPerson = controller.getAllPersons();

        assertThat(allPerson.getStatusCode()).isEqualTo(OK);
        assertThat(allPerson.getBody()).hasSize(0);
    }

    @Test
    void getPersonByID_whenPersonExists() {
        PersonController.PersonResponseModel expectedResponseModel =
                new PersonController.PersonResponseModel(1001L, "first one", 1989, List.of());
        Person expectedPerson = new Person(1001L, "first one", 1989, new HashSet<>());
        when(service.getPersonByName("first one")).thenReturn(Optional.of(expectedPerson));

        ResponseEntity<PersonController.PersonResponseModel> allPerson = controller.getPersonByName("first one");

        assertThat(allPerson.getStatusCode()).isEqualTo(OK);
        assertThat(allPerson.getBody()).isEqualTo(expectedResponseModel);
    }

    @Test
    void getPersonByID_whenPersonDoesNotExist() {
        when(service.getPersonByName("first one")).thenReturn(Optional.empty());

        ResponseEntity<PersonController.PersonResponseModel> allPerson = controller.getPersonByName("first one");

        assertThat(allPerson.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void createPerson() {
        Person newPerson = new Person("forth one", 2001);
        PersonController.PersonRequestModel requestBody =
                new PersonController.PersonRequestModel(newPerson.getName(), newPerson.getBorn());
        doNothing().when(service).createPerson(newPerson);

        ResponseEntity<Void> allPerson = controller.createPerson(requestBody);

        assertThat(allPerson.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void getPersonMoviesWatchedByName_returnsListOfMovies() {
        String clientName = "dummy name";
        List<String> watchedMovies = List.of("The Matrix", "The Matrix Reloaded");
        when(service.getPersonMoviesWatchedByName(clientName)).thenReturn(watchedMovies);

        ResponseEntity<List<String>> response = controller.getPersonMoviesWatchedByName(clientName);

        assertThat(response.getBody()).containsExactlyElementsOf(watchedMovies);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getPersonMoviesWatchedByNameWithRatings_returnsMoviesWithRatings() {
        String clientName = "dummy name";
        Map<String, Short> watchedMoviesWithRatings = Map.of("The Matrix", (short) 2, "The Matrix Reloaded", (short) 2);
        when(service.getPersonMoviesWatchedByNameWithRatings(clientName)).thenReturn(watchedMoviesWithRatings);

        ResponseEntity<Map<String, Short>> response = controller.getPersonMoviesWatchedByNameWithRatings(clientName);

        assertThat(response.getBody()).containsAllEntriesOf(watchedMoviesWithRatings);
        verifyNoMoreInteractions(service);
    }
}
