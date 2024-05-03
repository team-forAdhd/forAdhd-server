package com.project.foradhd.domain.auth.handler;

import com.project.foradhd.domain.auth.business.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        log.info("Logout Success");

        String userId = (String) authentication.getPrincipal();
        jwtService.deleteRefreshToken(userId);
        SecurityContextHolder.clearContext();
    }
}
