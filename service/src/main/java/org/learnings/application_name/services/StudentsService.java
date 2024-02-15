package org.learnings.application_name.services;

import org.learnings.application_name.repositories.StudentsEntity;
import org.learnings.application_name.repositories.StudentsRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.learnings.application_name.services.DTOEntityMapper.mapEntityToDTO;

@Component
public class StudentsService {

    private final StudentsRepository repository;

    //this would be better injected, but I don't want to add configuration files and complexity in the scope of this example
    private static final Map<UUID, StudentDTO> dataSource = new HashMap<>();

    static {
        UUID UUID1 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908");
        UUID UUID2 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc907");
        UUID UUID3 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc906");
        dataSource.put(UUID1, new StudentDTO(UUID1, "fullName1", 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))));
        dataSource.put(UUID2, new StudentDTO(UUID2, "fullName2", 5, Date.from(Instant.now().minus(Duration.ofDays(2)))));
        dataSource.put(UUID3, new StudentDTO(UUID3, "fullName3", 1, Date.from(Instant.now().minus(Duration.ofDays(30)))));
    }

    public StudentsService(StudentsRepository repository) {
        this.repository = repository;
    }

    public List<StudentDTO> getAllStudents() {
        return callToDataSource();
    }

    private List<StudentDTO> callToDataSource() {
        return dataSource.values().stream().toList();
    }

    public Optional<StudentDTO> getStudentByID(UUID requestID) {
        return Optional.ofNullable(dataSource.get(requestID));
    }

    public void createStudent(StudentDTO student) {
        if (!dataSource.containsKey(student.id()))
            dataSource.put(student.id(), student);
    }

    public Optional<StudentDTO> searchStudentByName(String name) {
        StudentsEntity byFullName = repository.findByFullName(name);
        if (byFullName == null) return Optional.empty();

        return Optional.of(mapEntityToDTO(byFullName));
    }
}
