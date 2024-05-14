package org.learnings.application_name.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentEntity {
    @Id
    private UUID id;
    private String fullName;
    private int currentSemester;
    private Timestamp entryDate;
}
