package org.learnings.application_name.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.services.StudentDTO;
import org.learnings.application_name.services.StudentsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("students")
public class StudentsController {

    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<StudentDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        log.debug("requested all students");
        Pageable pageable;

        if (sortBy != null && !sortBy.isBlank()) {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        return ResponseEntity.ok(studentsService.getAllStudents(pageable));
    }

    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentsController.CreateStudentRequestBody requestBody) {
        StudentDTO student = new StudentDTO(requestBody.id(), requestBody.fullname(), requestBody.currentSemester(), requestBody.entryDate());
        studentsService.createStudent(student);

        return ResponseEntity.ok().build();
    }

    public record CreateStudentRequestBody(@NotNull UUID id, @NotBlank String fullname, @Positive int currentSemester,
                                           @NotNull Date entryDate) {
    }
}
