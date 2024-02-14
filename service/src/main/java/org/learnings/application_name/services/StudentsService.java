package org.learnings.application_name.services;

import org.learnings.application_name.model.Student;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
public class StudentsService {

    //this would be better injected, but I don't want to add configuration files and complexity in the scope of this example
    private static final Map<UUID, Student> dataSource = new HashMap<>();

    static {
        UUID UUID1 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908");
        UUID UUID2 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc907");
        UUID UUID3 = UUID.fromString("f9734631-6833-4885-93c5-dd41679fc906");
        dataSource.put(UUID1, new Student(UUID1, "fullName1", 3, Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))));
        dataSource.put(UUID2, new Student(UUID2, "fullName2", 5, Date.from(Instant.now().minus(Duration.ofDays(2)))));
        dataSource.put(UUID3, new Student(UUID3, "fullName3", 1, Date.from(Instant.now().minus(Duration.ofDays(30)))));
    }

    public List<Student> getAllStudents() {
        return callToDataSource();
    }

    private List<Student> callToDataSource() {
        return dataSource.values().stream().toList();
    }

    public Optional<Student> getStudentByID(UUID requestID) {
        return Optional.ofNullable(dataSource.get(requestID));
    }

    public void createStudent(Student student) {
        if (!dataSource.containsKey(student.id()))
            dataSource.put(student.id(), student);
    }
}
