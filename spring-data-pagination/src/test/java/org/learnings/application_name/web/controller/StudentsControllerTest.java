package org.learnings.application_name.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.services.StudentDTO;
import org.learnings.application_name.services.StudentsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
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
    void getAllStudents_whenStudentsEmpty() {
        Pageable pageable = PageRequest.of(1, 5);
        when(service.getAllStudents(pageable)).thenReturn(Page.empty());

        ResponseEntity<Page<StudentDTO>> responseEntity = controller.getAllStudents(1, 5, null, false);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(0);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllStudents_whenNotSortedAndStudentsExist() {
        Pageable pageable = PageRequest.of(1, 5);
        PageImpl<StudentDTO> studentDTOS = new PageImpl<>(dataSource);
        when(service.getAllStudents(pageable)).thenReturn(studentDTOS);

        ResponseEntity<Page<StudentDTO>> responseEntity = controller.getAllStudents(1, 5, null, true);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(3);
        assertThat(responseEntity.getBody()).contains(expectedStudent);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllStudents_whenSortedAscAndStudentsExist() {
        Sort byFullNameAsc = Sort.by("fullName").ascending();
        Pageable pageable = PageRequest.of(1, 5, byFullNameAsc);
        PageImpl<StudentDTO> studentDTOS = new PageImpl<>(dataSource);
        when(service.getAllStudents(pageable)).thenReturn(studentDTOS);

        ResponseEntity<Page<StudentDTO>> responseEntity = controller.getAllStudents(1, 5, "fullName", true);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(3);
        assertThat(responseEntity.getBody()).contains(expectedStudent);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllStudents_whenSortedDescAndStudentsExist() {
        Sort byFullNameDesc = Sort.by("fullName").descending();
        Pageable pageable = PageRequest.of(1, 5, byFullNameDesc);
        PageImpl<StudentDTO> studentDTOS = new PageImpl<>(dataSource);
        when(service.getAllStudents(pageable)).thenReturn(studentDTOS);

        ResponseEntity<Page<StudentDTO>> responseEntity = controller.getAllStudents(1, 5, "fullName", false);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).hasSize(3);
        assertThat(responseEntity.getBody()).contains(expectedStudent);
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
}
