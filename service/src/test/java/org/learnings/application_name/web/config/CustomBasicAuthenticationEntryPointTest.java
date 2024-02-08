package org.learnings.application_name.web.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomBasicAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private final AuthenticationException authException = new AuthenticationServiceException("test exception");

    private final CustomBasicAuthenticationEntryPoint entryPoint = new CustomBasicAuthenticationEntryPoint();

    @Test
    void commence() throws IOException {
        doNothing().when(response).addHeader("WWW-Authenticate", "Basic realm=\"application_name\"");
        doNothing().when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

        entryPoint.commence(request, response, authException);

        verifyNoInteractions(request);
        verifyNoMoreInteractions(response);
    }
}
