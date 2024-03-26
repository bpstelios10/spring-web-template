package org.learnings.application_name.infrastructure.web.error;

import lombok.extern.slf4j.Slf4j;
import org.learnings.application_name.infrastructure.web.controller.InvalidUUIDException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Void> handleExceptions(Exception ex) {
        log.error("Unexpected Exception: [{}]", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler({InvalidUUIDException.class})
    public ResponseEntity<Void> handleInvalidUUIDExceptions(InvalidUUIDException ex) {
        log.error("InvalidUUIDException was thrown with message: [{}]", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
