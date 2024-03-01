package org.learnings.application_name.web.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.debug("Resource was not found in DB. Exception message is: [{}]", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
