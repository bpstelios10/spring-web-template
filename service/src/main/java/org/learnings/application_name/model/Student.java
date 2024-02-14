package org.learnings.application_name.model;

import java.util.Date;
import java.util.UUID;

public record Student(UUID id, String fullName, int currentSemester, Date entryDate) {
}
