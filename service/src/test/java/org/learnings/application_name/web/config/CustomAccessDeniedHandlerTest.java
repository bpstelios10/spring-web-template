package org.learnings.application_name.web.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    private final CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private final AccessDeniedException accessDeniedException = new AccessDeniedException("test exception");

    @Test
    void handle() throws IOException {
        doNothing().when(response).setHeader("WWW-Authenticate", "Basic realm=\"application_name\"");
        doNothing().when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, accessDeniedException.getMessage());

        handler.handle(request, response, accessDeniedException);

        verifyNoInteractions(request);
        verifyNoMoreInteractions(response);
    }
}
