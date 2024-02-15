package org.learnings.application_name.services;

import org.learnings.application_name.repositories.StudentsEntity;

public class DTOEntityMapper {
    private DTOEntityMapper() {
        throw new UnsupportedOperationException();
    }

    public static StudentsEntity mapDTOToEntity(StudentDTO dto) {
        return new StudentsEntity(dto.id(), dto.fullName(), dto.currentSemester(), dto.entryDate());
    }

    public static StudentDTO mapEntityToDTO(StudentsEntity entity) {
        return new StudentDTO(entity.getId(), entity.getFullName(), entity.getCurrentSemester(), entity.getEntryDate());
    }
}
