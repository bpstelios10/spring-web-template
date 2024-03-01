package org.learnings.application_name.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.model.Student;
import org.learnings.application_name.services.StudentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("students")
public class StudentsController {

    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        log.debug("requested all students");

        return ResponseEntity.ok(studentsService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentByID(@PathVariable String id) {
        log.debug("requested student with id [{}]", id);
        //add check to validate id is uuid
        UUID requestID = UUID.fromString(id);

        return ResponseEntity.ok(studentsService.getStudentByID(requestID).orElse(null));
    }

    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentsController.CreateStudentRequestBody requestBody) {
        Student student = new Student(requestBody.id(), requestBody.fullname(), requestBody.currentSemester(), requestBody.entryDate());
        studentsService.createStudent(student);

        return ResponseEntity.ok().build();
    }

    record CreateStudentRequestBody(@NotNull UUID id, @NotBlank String fullname, @Positive int currentSemester,
                                    @NotNull Date entryDate) {
    }
}
