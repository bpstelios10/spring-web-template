package org.learnings.application_name.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.model.Person;
import org.learnings.application_name.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseModel>> getAllPersons() {
        log.debug("requested all person");

        return ResponseEntity.ok(
                personService.getAllPersons()
                        .stream()
                        .map(PersonResponseModel::fromDomainObject)
                        .toList());
    }

    @GetMapping("/{name}")
    public ResponseEntity<PersonResponseModel> getPersonByName(@PathVariable @NotBlank String name) {
        log.debug("requested person with name: [{}]", name);

        return ResponseEntity.ok(
                personService.getPersonByName(name)
                        .map(PersonResponseModel::fromDomainObject)
                        .orElse(null));
    }

    @PostMapping
    public ResponseEntity<Void> createPerson(@Valid @RequestBody PersonController.PersonRequestModel requestBody) {
        personService.createPerson(requestBody.toDomainObject());

        return ResponseEntity.ok().build();
    }

    record PersonRequestModel(@NotBlank String name, @Positive int born) {
        Person toDomainObject() {
            return new Person(name, born);
        }
    }

    record PersonResponseModel(@NotNull Long id, @NotBlank String name, @Positive int born) {
        static PersonResponseModel fromDomainObject(Person person) {
            return new PersonResponseModel(person.getId(), person.getName(), person.getBorn());
        }
    }
}
