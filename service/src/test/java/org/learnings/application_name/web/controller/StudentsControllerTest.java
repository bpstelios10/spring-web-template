package org.learnings.application_name.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.model.Student;
import org.learnings.application_name.services.StudentsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    @Mock
    private StudentsService service;
    @InjectMocks
    private StudentsController controller;

    private final List<Student> dataSource = List.of(
            new Student(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"), "att1", 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))),
            new Student(UUID.randomUUID(), "att2", 5, Date.from(Instant.now().minus(Duration.ofDays(2)))),
            new Student(UUID.randomUUID(), "att3", 1, Date.from(Instant.now().minus(Duration.ofDays(30))))
    );

    @Test
    void getAllStudents_whenStudentsExist() {
        when(service.getAllStudents()).thenReturn(dataSource);
        Student expectedStudent =
                new Student(
                        UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                        "att1",
                        3,
                        Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
                );

        ResponseEntity<List<Student>> responseEntity = controller.getAllStudents();

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(3);
        assertThat(responseEntity.getBody()).contains(expectedStudent);
    }

    @Test
    void getAllStudents_whenStudentsEmpty() {
        when(service.getAllStudents()).thenReturn(List.of());

        ResponseEntity<List<Student>> responseEntity = controller.getAllStudents();

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(0);
    }

    @Test
    void getStudentByID_whenStudentsExist() {
        Student expectedStudent =
                new Student(
                        UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                        "att1",
                        3,
                        Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
                );
        when(service.getStudentByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"))).thenReturn(Optional.of(expectedStudent));

        ResponseEntity<Student> responseEntity = controller.getStudentByID("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedStudent);
    }

    @Test
    void getStudentByID_whenStudentsEmpty() {
        when(service.getStudentByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"))).thenReturn(Optional.empty());

        ResponseEntity<Student> responseEntity = controller.getStudentByID("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void createStudent_succeeds() {
        Student expectedStudent =
                new Student(
                        UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                        "att1",
                        3,
                        Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
                );
        StudentsController.CreateStudentRequestBody requestBody = new StudentsController.CreateStudentRequestBody(
                expectedStudent.id(), expectedStudent.fullName(), expectedStudent.currentSemester(), expectedStudent.entryDate());
        doNothing().when(service).createStudent(expectedStudent);

        ResponseEntity<Void> responseEntity = controller.createStudent(requestBody);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }
}
