package org.learnings.application_name.infrastructure.web.controller;

public class InvalidUUIDException extends RuntimeException {
    public InvalidUUIDException(String stringUUID) {
        super("Provided String [" + stringUUID + "] is not a valid UUID");
    }
}
