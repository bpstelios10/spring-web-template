package org.learnings.application_name.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentsRepositoryTest {

    @Autowired
    private StudentsRepository repository;
    private StudentEntity student;
    private static final UUID studentID = UUID.randomUUID();
    private static final Timestamp studentRecordTimestamp = Timestamp.from(Instant.now());

    @BeforeEach
    public void setUp() {
        student = new StudentEntity(studentID, "John Wick", 5, studentRecordTimestamp);
        repository.save(student);
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    void findById_whenRecordDoesNotExist_givesBackEmptyResult() {
        Optional<StudentEntity> responseStudent = repository.findById(UUID.randomUUID());

        assertThat(responseStudent).isNotPresent();
    }

    @Test
    void findById_whenRecordExists_givesResultBack() {
        Optional<StudentEntity> responseStudent = repository.findById(studentID);

        assertThat(responseStudent).isPresent();
        assertThat(responseStudent.get()).isEqualTo(student);
    }

    @Test
    void findAllPaginated_whenRecordsExist_givesBackListOfRecords() {
        StudentEntity anotherStudent = new StudentEntity(UUID.randomUUID(), "Nio", 2, Timestamp.from(Instant.now()));
        repository.save(anotherStudent);

        Pageable pageable = PageRequest.of(0, 5);
        Page<StudentEntity> allStudents = repository.findAll(pageable);

        assertThat(allStudents).hasSize(2);
        assertThat(allStudents).containsExactly(student, anotherStudent);
    }

    @Test
    void findAllPaginated_whenRecordsExist_givesBackRecordsInPages() {
        StudentEntity anotherStudent = new StudentEntity(UUID.randomUUID(), "Nio", 2, Timestamp.from(Instant.now()));
        repository.save(anotherStudent);

        // page 1
        Pageable pageable = PageRequest.of(0, 1);
        Page<StudentEntity> allStudents = repository.findAll(pageable);

        assertThat(allStudents).hasSize(1);
        assertThat(allStudents).containsExactly(student);

        // page 2
        pageable = PageRequest.of(1, 1);
        allStudents = repository.findAll(pageable);

        assertThat(allStudents).hasSize(1);
        assertThat(allStudents).containsExactly(anotherStudent);
    }

    @Test
    void findAllPaginatedSorted_whenRecordsExist_givesBackRecordsInPages() {
        StudentEntity anotherStudent = new StudentEntity(UUID.randomUUID(), "Nio", 2, Timestamp.from(Instant.now()));
        repository.save(anotherStudent);

        // page 1
        Sort byFullNameDescending = Sort.by("fullName").descending();
        Pageable pageable = PageRequest.of(0, 1, byFullNameDescending);
        Page<StudentEntity> allStudents = repository.findAll(pageable);

        assertThat(allStudents).hasSize(1);
        assertThat(allStudents).containsExactly(anotherStudent);

        // page 2
        pageable = PageRequest.of(1, 1, byFullNameDescending);
        allStudents = repository.findAll(pageable);

        assertThat(allStudents).hasSize(1);
        assertThat(allStudents).containsExactly(student);
    }

    @Test
    void save_whenNewRecordAdded_addsRecordInDatabase() {
        StudentEntity anotherStudent = new StudentEntity(UUID.randomUUID(), "Nio", 2, Timestamp.from(Instant.now()));
        int initialNumberOfRecords = repository.findAll().size();

        repository.save(anotherStudent);

        assertThat(repository.findAll().size()).isEqualTo(initialNumberOfRecords + 1);
    }

    @Test
    void save_whenNewRecordExists_doesNotAddRecordInDatabase() {
        StudentEntity anotherStudent = new StudentEntity(UUID.randomUUID(), "Nio", 2, Timestamp.from(Instant.now()));
        repository.save(anotherStudent);
        int initialNumberOfRecords = repository.findAll().size();

        repository.save(anotherStudent);

        assertThat(repository.findAll().size()).isEqualTo(initialNumberOfRecords);
    }

    @Test
    void save_whenRecordExistsButWithChanges_doesUpdate() {
        StudentEntity anotherStudent = new StudentEntity(student.getId(), student.getFullName(), 2, Timestamp.from(Instant.now()));

        repository.save(anotherStudent);
        Optional<StudentEntity> responseStudent = repository.findById(studentID);

        assertThat(responseStudent).isPresent();
        assertThat(responseStudent.get().getCurrentSemester()).isEqualTo(2);
    }

    @Test
    void delete_whenRecordExists_removesRecord() {
        int initialNumberOfRecords = repository.findAll().size();

        repository.delete(student);

        assertThat(repository.findAll().size()).isEqualTo(initialNumberOfRecords - 1);
    }

    @Test
    void delete_whenRecordDoesNotExist_doesNothing() {
        StudentEntity anotherStudent = new StudentEntity(UUID.randomUUID(), "Nio", 2, Timestamp.from(Instant.now()));
        int initialNumberOfRecords = repository.findAll().size();

        repository.delete(anotherStudent);

        assertThat(repository.findAll().size()).isEqualTo(initialNumberOfRecords);
    }
}
