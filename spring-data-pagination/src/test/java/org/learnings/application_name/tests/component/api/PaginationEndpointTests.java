package org.learnings.application_name.tests.component.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnings.application_name.repositories.StudentsRepository;
import org.learnings.application_name.services.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.learnings.application_name.services.DTOEntityMapper.mapDTOToEntity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("component-test")
public class PaginationEndpointTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentsRepository repository;
    private final StudentDTO expectedStudent =
            new StudentDTO(
                    UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                    "fullName1",
                    3,
                    Date.from(Instant.parse("2024-02-01T08:15:24.00Z"))
            );

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void getAllStudents_whenStudentsEmpty() throws Exception {
        mockMvc.perform(get("/students/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(3));
    }

    @Test
    void getAllStudents_whenStudentInPage() throws Exception {
        repository.save(mapDTOToEntity(expectedStudent));

        mockMvc.perform(get("/students/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("fullName1"))
                .andExpect(jsonPath("$.empty").value(false))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(3));
    }

    @Test
    void getAllStudents_whenStudentsNotInPage() throws Exception {
        repository.save(mapDTOToEntity(expectedStudent));

        mockMvc.perform(
                        get("/students/")
                                .param("page", "1")
                                .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(0))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(2));
    }

    @Test
    void getAllStudents_whenMultipleStudentsNotSorted_thenReturnTheOrderStudentsWereStored() throws Exception {
        StudentDTO student2 = new StudentDTO(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc907"),
                "fullName2",
                5,
                Date.from(Instant.parse("2023-02-02T09:55:24.00Z"))
        );
        StudentDTO student3 = new StudentDTO(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc906"),
                "fullName3",
                1,
                Date.from(Instant.parse("2025-02-11T10:11:24.00Z"))
        );
        repository.save(mapDTOToEntity(expectedStudent));
        repository.save(mapDTOToEntity(student3));
        repository.save(mapDTOToEntity(student2));

        mockMvc.perform(
                        get("/students/")
                                .param("page", "0")
                                .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("fullName1"))
                .andExpect(jsonPath("$.content[1].fullName").value("fullName3"))
                .andExpect(jsonPath("$.empty").value(false))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.sort.sorted").value(false));

        mockMvc.perform(
                        get("/students/")
                                .param("page", "1")
                                .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("fullName2"))
                .andExpect(jsonPath("$.empty").value(false))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.sort.sorted").value(false));
    }

    @Test
    void getAllStudents_whenMultipleStudentsSorted_thenReturnStudentsSorted() throws Exception {
        StudentDTO student2 = new StudentDTO(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc907"),
                "fullName2",
                5,
                Date.from(Instant.parse("2023-02-02T09:55:24.00Z"))
        );
        StudentDTO student3 = new StudentDTO(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc906"),
                "fullName3",
                1,
                Date.from(Instant.parse("2025-02-11T10:11:24.00Z"))
        );
        repository.save(mapDTOToEntity(expectedStudent));
        repository.save(mapDTOToEntity(student3));
        repository.save(mapDTOToEntity(student2));

        mockMvc.perform(
                        get("/students/")
                                .param("page", "0")
                                .param("size", "2")
                                .param("sortBy", "fullName")
                                .param("ascending", "false")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("fullName3"))
                .andExpect(jsonPath("$.content[1].fullName").value("fullName2"))
                .andExpect(jsonPath("$.empty").value(false))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.sort.sorted").value(true));

        mockMvc.perform(
                        get("/students/")
                                .param("page", "1")
                                .param("size", "2")
                                .param("sortBy", "fullName")
                                .param("ascending", "false")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("fullName1"))
                .andExpect(jsonPath("$.empty").value(false))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.sort.sorted").value(true));
    }
}
