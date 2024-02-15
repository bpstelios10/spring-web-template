package org.learnings.application_name.services;

import java.util.Date;
import java.util.UUID;

public record StudentDTO(UUID id, String fullName, int currentSemester, Date entryDate) {
}
