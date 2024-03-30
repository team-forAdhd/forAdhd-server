package com.project.foradhd.domain.auth.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthorizationFailureHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        log.error("Authorization Fail: {}", accessDeniedException.getMessage());
        response.setStatus(SC_FORBIDDEN);
        response.getWriter().write(accessDeniedException.getMessage());
    }
}
