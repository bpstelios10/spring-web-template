package org.learnings.application_name.tests.component.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.repositories.StudentsRepository;
import org.learnings.application_name.services.StudentDTO;
import org.learnings.application_name.services.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.learnings.application_name.services.DTOEntityMapper.mapDTOToEntity;

/**
 * Testing spring-data-jpa repositories
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("component-test")
public class StudentsDBTests {

    @Autowired
    private StudentsRepository repository;
    @Autowired
    private StudentsService service;
    private final StudentDTO expectedStudent =
            new StudentDTO(
                    UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                    "fullName1",
                    3,
                    Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
            );

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void getAllStudents_whenNoneExists() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<StudentDTO> allStudents = service.getAllStudents(pageable);

        assertThat(allStudents).hasSize(0);
    }

    @Test
    void getAllStudents_whenSomeExist() {
        repository.save(mapDTOToEntity(expectedStudent));
        Pageable pageable = PageRequest.of(0, 2);

        Page<StudentDTO> allStudents = service.getAllStudents(pageable);

        assertThat(allStudents).containsExactly(expectedStudent);
    }

    @Test
    void createStudent_whenStudentExists() {
        service.createStudent(expectedStudent);
        assertDoesNotThrow(() -> service.createStudent(expectedStudent));
    }
}
