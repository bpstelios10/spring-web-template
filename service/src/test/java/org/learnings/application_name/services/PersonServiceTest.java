package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.infrastructure.repositories.PersonRepository;
import org.learnings.application_name.model.Person;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repository;
    @InjectMocks
    private PersonService service;

    private final Person expectedPerson = new Person(1001L, "first one", 1989);
    private final List<Person> dataSource = List.of(
            expectedPerson,
            new Person(1002L, "second one", 1999),
            new Person(1003L, "third one", 1980)
    );

    @Test
    void getAllPersons() {
        when(repository.findAll()).thenReturn(dataSource);

        List<Person> allPersons = service.getAllPersons();

        assertThat(allPersons).hasSize(3);
        assertThat(allPersons).contains(expectedPerson);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getPersonByID_whenPersonExists() {
        when(repository.findByName("first one")).thenReturn(Optional.of(expectedPerson));

        Optional<Person> personByName = service.getPersonByName("first one");

        assertThat(personByName).isNotEmpty();
        assertThat(personByName.get()).isEqualTo(expectedPerson);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getPersonByID_whenPersonNotExists() {
        when(repository.findByName("first one")).thenReturn(Optional.empty());

        Optional<Person> personByName = service.getPersonByName("first one");

        assertThat(personByName).isEmpty();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createPerson_whenPersonNotExists() {
        when(repository.findByName(expectedPerson.getName())).thenReturn(Optional.empty());
        when(repository.save(expectedPerson)).thenReturn(expectedPerson);

        service.createPerson(expectedPerson);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void createPerson_whenPersonExists() {
        when(repository.findByName(expectedPerson.getName())).thenReturn(Optional.of(expectedPerson));

        service.createPerson(expectedPerson);

        verifyNoMoreInteractions(repository);
    }
}
