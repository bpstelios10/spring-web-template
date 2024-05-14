package org.learnings.application_name.services;

import org.learnings.application_name.repositories.StudentEntity;

import java.sql.Timestamp;
import java.util.Date;

public class DTOEntityMapper {
    private DTOEntityMapper() {
        throw new UnsupportedOperationException();
    }

    public static StudentEntity mapDTOToEntity(StudentDTO dto) {
        return new StudentEntity(dto.id(), dto.fullName(), dto.currentSemester(), new Timestamp(dto.entryDate().getTime()));
    }

    public static StudentDTO mapEntityToDTO(StudentEntity entity) {
        return new StudentDTO(entity.getId(), entity.getFullName(), entity.getCurrentSemester(), new Date(entity.getEntryDate().getTime()));
    }
}
