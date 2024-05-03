package com.project.foradhd.domain.auth.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.auth.business.userdetails.impl.UserDetailsImpl;
import com.project.foradhd.domain.auth.web.dto.response.LoginResponse;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.global.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails.getUserId(), userDetails.getUsername(),
            userDetails.getAuthorities());
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUserId());
        jwtService.saveRefreshToken(userDetails.getUserId(), refreshToken);

        Boolean hasVerifiedEmail = userService.hasVerifiedEmail(userDetails.getUserId());
        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken, hasVerifiedEmail);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtil.writeValueAsString(loginResponse));
    }
}
