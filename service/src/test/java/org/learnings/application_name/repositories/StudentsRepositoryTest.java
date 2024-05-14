package org.learnings.application_name.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
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
    void findAll_whenRecordsExist_givesBackListOfRecords() {
        StudentEntity anotherStudent = new StudentEntity(UUID.randomUUID(), "Nio", 2, Timestamp.from(Instant.now()));
        repository.save(anotherStudent);

        List<StudentEntity> allStudents = repository.findAll();

        assertThat(allStudents).hasSize(2);
        assertThat(allStudents).containsExactly(student, anotherStudent);
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

    @Test
    void findByFullName_whenRecordDoesNotExist_givesBackEmptyResult() {
        List<StudentEntity> responseStudent = repository.findByFullName("Not Valid");

        assertThat(responseStudent).isEmpty();
    }

    @Test
    void findByFullName_whenRecordExists_givesResultBack() {
        List<StudentEntity> allFoundByName = repository.findByFullName("John");

        assertThat(allFoundByName).hasSize(1);
        assertThat(allFoundByName).containsExactly(student);
    }
}
