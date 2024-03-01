package org.learnings.application_name.services;

import org.learnings.application_name.repositories.StudentsEntity;

import java.sql.Timestamp;
import java.util.Date;

public class DTOEntityMapper {
    private DTOEntityMapper() {
        throw new UnsupportedOperationException();
    }

    public static StudentsEntity mapDTOToEntity(StudentDTO dto) {
        return new StudentsEntity(dto.id(), dto.fullName(), dto.currentSemester(), new Timestamp(dto.entryDate().getTime()));
    }

    public static StudentDTO mapEntityToDTO(StudentsEntity entity) {
        return new StudentDTO(entity.getId(), entity.getFullName(), entity.getCurrentSemester(), new Date(entity.getEntryDate().getTime()));
    }
}
