package org.learnings.application_name.services;

import org.learnings.application_name.infrastructure.repositories.PersonRepository;
import org.learnings.application_name.model.Person;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    public Optional<Person> getPersonByName(String name) {

        return repository.findByName(name);
    }

    public List<String> getPersonMoviesWatchedByName(String name) {
        return repository.findWatchedMoviesByName(name).getMoviesWatched();
    }

    public Map<String, Short> getPersonMoviesWatchedByNameWithRatings(String name) {
        return repository.findWatchedMoviesByName(name).getMoviesAndRatingsWatched();
    }

    public void createPerson(Person person) {
        if (repository.findByName(person.getName()).isEmpty())
            repository.save(person);
    }
}
