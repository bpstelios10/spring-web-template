package org.learnings.application_name.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.services.StudentDTO;
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
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    @Mock
    private StudentsService service;
    @InjectMocks
    private StudentsController controller;

    private final List<StudentDTO> dataSource = List.of(
            new StudentDTO(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"), "att1", 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))),
            new StudentDTO(UUID.randomUUID(), "att2", 5, Date.from(Instant.now().minus(Duration.ofDays(2)))),
            new StudentDTO(UUID.randomUUID(), "att3", 1, Date.from(Instant.now().minus(Duration.ofDays(30))))
    );
    private final StudentDTO expectedStudent = dataSource.getFirst();

    @Test
    void getAllStudents_whenStudentsExist() {
        when(service.getAllStudents()).thenReturn(dataSource);

        ResponseEntity<List<StudentDTO>> responseEntity = controller.getAllStudents();

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(3);
        assertThat(responseEntity.getBody()).contains(expectedStudent);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllStudents_whenStudentsEmpty() {
        when(service.getAllStudents()).thenReturn(List.of());

        ResponseEntity<List<StudentDTO>> responseEntity = controller.getAllStudents();

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(0);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getStudentByID_whenStudentsExist() {
        when(service.getStudentByID(expectedStudent.id())).thenReturn(Optional.of(expectedStudent));

        ResponseEntity<StudentDTO> responseEntity = controller.getStudentByID("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedStudent);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getStudentByID_whenStudentsEmpty() {
        when(service.getStudentByID(expectedStudent.id())).thenReturn(Optional.empty());

        ResponseEntity<StudentDTO> responseEntity = controller.getStudentByID("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        verifyNoMoreInteractions(service);
    }

    @Test
    void createStudent_succeeds() {
        StudentsController.CreateStudentRequestBody requestBody = new StudentsController.CreateStudentRequestBody(
                expectedStudent.id(), expectedStudent.fullName(), expectedStudent.currentSemester(), expectedStudent.entryDate());
        doNothing().when(service).createStudent(expectedStudent);

        ResponseEntity<Void> responseEntity = controller.createStudent(requestBody);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        verifyNoMoreInteractions(service);
    }

    @Test
    void searchStudentByName_whenStudentsExist() {
        when(service.searchStudentByName(expectedStudent.fullName())).thenReturn(List.of(expectedStudent));

        ResponseEntity<List<StudentDTO>> responseEntity = controller.searchStudentByName("att1");

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).containsExactly(expectedStudent);
        verifyNoMoreInteractions(service);
    }

    @Test
    void searchStudentByName_whenStudentsEmpty() {
        when(service.searchStudentByName(expectedStudent.fullName())).thenReturn(List.of());

        ResponseEntity<List<StudentDTO>> responseEntity = controller.searchStudentByName("att1");

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        verifyNoMoreInteractions(service);
    }
}
