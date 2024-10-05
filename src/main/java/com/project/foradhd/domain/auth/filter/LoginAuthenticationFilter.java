package com.project.foradhd.domain.auth.filter;

import com.project.foradhd.domain.auth.web.dto.request.LoginRequest;
import com.project.foradhd.global.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String REQUEST_LOG_FORMAT = """
            [REQUEST]
            API : {} {}
            ClientIp : {}
            Body : {}
            """;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = extractLoginRequest(request);
        log.info(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestURI(), request.getRemoteAddr(),
                JsonUtil.writeValueAsString(loginRequest));

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
            .unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        return getAuthenticationManager().authenticate(authRequest);
    }

    private LoginRequest extractLoginRequest(HttpServletRequest request) {
        try {
            return JsonUtil.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException | RuntimeException e) {
            log.error("Invalid Login Form: {}", e.getMessage());
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
