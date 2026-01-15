package org.learnings.application_name.web.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

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
    void handleResponseStatusException_returnsStatusCodeDefined() {
        ResponseStatusException someException =
                new ResponseStatusException(HttpStatus.BAD_GATEWAY, "test exception thrown");

        ResponseEntity<String> actualResponse = controllerExceptionHandler.handleResponseStatusException(someException);

        assertThat(HttpStatus.BAD_GATEWAY).isEqualTo(actualResponse.getStatusCode());
        assertThat("test exception thrown").isEqualTo(actualResponse.getBody());
    }
}
