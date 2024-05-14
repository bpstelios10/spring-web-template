package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.repositories.StudentEntity;
import org.learnings.application_name.repositories.StudentsRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.learnings.application_name.services.DTOEntityMapper.mapDTOToEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentsServiceTest {

    @Mock
    private StudentsRepository repository;
    @InjectMocks
    private StudentsService service;
    private final StudentDTO expectedStudent =
            new StudentDTO(
                    UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                    "fullName1",
                    3,
                    Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
            );

    @Test
    void getAllStudents_whenNoneExists() {
        when(repository.findAll()).thenReturn(List.of());

        List<StudentDTO> allStudents = service.getAllStudents();

        assertThat(allStudents).hasSize(0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllStudents_whenSomeExist() {
        when(repository.findAll()).thenReturn(List.of(mapDTOToEntity(expectedStudent)));

        List<StudentDTO> allStudents = service.getAllStudents();

        assertThat(allStudents).hasSize(1);
        assertThat(allStudents).contains(expectedStudent);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getStudentByID_whenStudentExists() {
        UUID expectedStudentId = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908");
        when(repository.findById(expectedStudentId)).thenReturn(Optional.of(mapDTOToEntity(expectedStudent)));

        Optional<StudentDTO> studentByID = service.getStudentByID(expectedStudentId);

        assertThat(studentByID).isNotEmpty();
        assertThat(studentByID.get()).isEqualTo(expectedStudent);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getStudentByID_whenStudentNotExists() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        Optional<StudentDTO> studentByID = service.getStudentByID(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc909"));

        assertThat(studentByID).isEmpty();
    }

    @Test
    void createStudent_whenStudentNotExists() {
        StudentEntity expectedStudentEntity = mapDTOToEntity(expectedStudent);
        when(repository.findById(expectedStudent.id())).thenReturn(Optional.empty());
        when(repository.save(expectedStudentEntity)).thenReturn(expectedStudentEntity);

        service.createStudent(expectedStudent);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void createStudent_whenStudentExists() {
        StudentEntity expectedStudentEntity = mapDTOToEntity(expectedStudent);
        when(repository.findById(expectedStudent.id())).thenReturn(Optional.of(expectedStudentEntity));

        service.createStudent(expectedStudent);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void searchStudentByName_whenStudentNotExists() {
        when(repository.findByFullName(expectedStudent.fullName())).thenReturn(List.of());

        List<StudentDTO> responseStudent = service.searchStudentByName("fullName1");

        assertThat(responseStudent).isEmpty();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void searchStudentByName_whenStudentExists() {
        StudentEntity expectedStudentEntity = mapDTOToEntity(expectedStudent);
        when(repository.findByFullName(expectedStudent.fullName())).thenReturn(List.of(expectedStudentEntity));

        List<StudentDTO> responseStudent = service.searchStudentByName("fullName1");

        assertThat(responseStudent).isNotEmpty();
        assertThat(responseStudent).containsExactly(expectedStudent);
        verifyNoMoreInteractions(repository);
    }
}
