package org.learnings.application_name.services;

import org.learnings.application_name.repositories.StudentsRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.learnings.application_name.services.DTOEntityMapper.mapDTOToEntity;

@Component
public class StudentsService {

    private final StudentsRepository repository;

    public StudentsService(StudentsRepository repository) {
        this.repository = repository;
    }

    public List<StudentDTO> getAllStudents() {
        return repository.findAll().stream().map(DTOEntityMapper::mapEntityToDTO).toList();
    }

    public Optional<StudentDTO> getStudentByID(UUID requestID) {
        return repository.findById(requestID)
                .map(DTOEntityMapper::mapEntityToDTO);
    }

    public void createStudent(StudentDTO student) {
        if (repository.findById(student.id()).isEmpty())
            repository.save(mapDTOToEntity(student));
    }

    public List<StudentDTO> searchStudentByName(String name) {
        return repository.findByFullName(name).stream().map(DTOEntityMapper::mapEntityToDTO).toList();
    }
}
