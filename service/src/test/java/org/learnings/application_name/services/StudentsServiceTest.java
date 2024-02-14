package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.learnings.application_name.model.Student;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StudentsServiceTest {

    private final StudentsService service = new StudentsService();
    private final Student expectedStudent =
            new Student(
                    UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                    "fullName1",
                    3,
                    Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
            );

    @Test
    void getAllStudents() {
        List<Student> allStudents = service.getAllStudents();

        assertThat(allStudents).hasSize(3);
        assertThat(allStudents).contains(expectedStudent);
    }

    @Test
    void getStudentByID_whenStudentExists() {
        Optional<Student> studentByID = service.getStudentByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"));

        assertThat(studentByID).isNotEmpty();
        assertThat(studentByID.get()).isEqualTo(expectedStudent);
    }

    @Test
    void getStudentByID_whenStudentNotExists() {
        Optional<Student> studentByID = service.getStudentByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc909"));

        assertThat(studentByID).isEmpty();
    }

    @Test
    void createStudent_whenStudentNotExists() {
        service.createStudent(new Student(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc904"),
                "fullName1",
                3,
                Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
        ));

        assertThat(service.getAllStudents()).hasSize(4);
    }

    @Test
    void createStudent_whenStudentExists() {
        service.createStudent(expectedStudent);

        assertThat(service.getAllStudents()).hasSize(3);
    }
}