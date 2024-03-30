package com.project.foradhd.global.util;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HeaderUtil {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    public Optional<String> parseToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) &&
            authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            return Optional.of(authorizationHeader.substring(BEARER_TOKEN_PREFIX.length()));
        }
        return Optional.empty();
    }
}
