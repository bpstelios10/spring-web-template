package org.learnings.application_name.infrastructure.web.error;

import org.junit.jupiter.api.Test;
import org.learnings.application_name.infrastructure.web.controller.InvalidUUIDException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

    @Test
    void handleExceptions() {
        Exception someException = new RuntimeException("test exception thrown");
        ResponseEntity<Void> expectedResponse = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        ResponseEntity<Void> actualResponse = controllerExceptionHandler.handleExceptions(someException);

        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void handleInvalidUUIDExceptions() {
        InvalidUUIDException someException = new InvalidUUIDException("23401234324132");
        ResponseEntity<Void> expectedResponse = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();

        ResponseEntity<Void> actualResponse = controllerExceptionHandler.handleInvalidUUIDExceptions(someException);

        assertThat(expectedResponse).isEqualTo(actualResponse);
    }
}
