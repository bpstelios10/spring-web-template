package org.learnings.application_name.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.model.Person;
import org.learnings.application_name.services.PersonService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService service;
    @InjectMocks
    private PersonController controller;

    private final List<Person> dataSource = List.of(
            new Person(1001L, "first one", 1989),
            new Person(1002L, "second one", 1999),
            new Person(1003L, "third one", 1980)
    );

    @Test
    void getAllPersons_whenPersonsExist() {
        when(service.getAllPersons()).thenReturn(dataSource);
        PersonController.PersonResponseModel expectedPerson =
                new PersonController.PersonResponseModel(1001L, "first one", 1989);

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
                new PersonController.PersonResponseModel(1001L, "first one", 1989);
        Person expectedPerson = new Person(1001L, "first one", 1989);
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
        Person newPerson = new Person(null, "forth one", 2001);
        PersonController.PersonRequestModel requestBody =
                new PersonController.PersonRequestModel(newPerson.getName(), newPerson.getBorn());
        doNothing().when(service).createPerson(newPerson);

        ResponseEntity<Void> allPerson = controller.createPerson(requestBody);

        assertThat(allPerson.getStatusCode()).isEqualTo(OK);
    }
}
