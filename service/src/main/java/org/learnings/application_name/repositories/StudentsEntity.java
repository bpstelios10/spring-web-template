package org.learnings.application_name.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentsEntity {
    @Id
    private UUID id;
    private String fullName;
    private int currentSemester;
    private Date entryDate;
}
