package com.example.student_management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String msg = (accessDeniedException != null && accessDeniedException.getMessage() != null)
                ? accessDeniedException.getMessage().replace("\"", "\\\"")
                : "Access denied for this resource";

        String json = "{\"timestamp\":\"" + LocalDateTime.now() + "\","
                + "\"status\":403,"
                + "\"error\":\"Forbidden\","
                + "\"message\":\"" + msg + "\","
                + "\"path\":\"" + request.getRequestURI() + "\"}";

        response.getWriter().write(json);
    }
}
