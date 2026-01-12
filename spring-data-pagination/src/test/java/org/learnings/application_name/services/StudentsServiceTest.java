package org.learnings.application_name.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.repositories.StudentEntity;
import org.learnings.application_name.repositories.StudentsRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.learnings.application_name.services.DTOEntityMapper.mapDTOToEntity;
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
        Pageable pageable = PageRequest.of(1, 5);
        when(repository.findAll(pageable)).thenReturn(Page.empty());

        Page<StudentDTO> allStudents = service.getAllStudents(pageable);

        assertThat(allStudents).hasSize(0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllStudents_whenSomeExist() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<StudentEntity> pages = new PageImpl<>(List.of(mapDTOToEntity(expectedStudent)));

        when(repository.findAll(pageable)).thenReturn(pages);

        Page<StudentDTO> allStudents = service.getAllStudents(pageable);

        assertThat(allStudents).hasSize(1);
        assertThat(allStudents).contains(expectedStudent);
        verifyNoMoreInteractions(repository);
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
}
