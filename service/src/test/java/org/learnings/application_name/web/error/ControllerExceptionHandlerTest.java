package org.learnings.application_name.web.error;

import org.junit.jupiter.api.Test;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
    void handleResourceNotFoundExceptions() {
        ResourceNotFoundException someException = new ResourceNotFoundException();
        ResponseEntity<Void> expectedResponse = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();

        ResponseEntity<Void> actualResponse = controllerExceptionHandler.handleResourceNotFoundException(someException);

        assertThat(expectedResponse).isEqualTo(actualResponse);
    }
}
