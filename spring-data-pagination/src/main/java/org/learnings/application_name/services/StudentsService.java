package org.learnings.application_name.services;

import org.learnings.application_name.repositories.StudentsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static org.learnings.application_name.services.DTOEntityMapper.mapDTOToEntity;

@Component
public class StudentsService {

    private final StudentsRepository repository;

    public StudentsService(StudentsRepository repository) {
        this.repository = repository;
    }

    public Page<StudentDTO> getAllStudents(Pageable pageable) {
        return repository.findAll(pageable).map(DTOEntityMapper::mapEntityToDTO);
    }

    public void createStudent(StudentDTO student) {
        if (repository.findById(student.id()).isEmpty())
            repository.save(mapDTOToEntity(student));
    }
}
