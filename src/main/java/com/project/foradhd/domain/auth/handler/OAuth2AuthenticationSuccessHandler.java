package com.project.foradhd.domain.auth.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.auth.business.userdetails.impl.OAuth2UserImpl;
import com.project.foradhd.domain.auth.web.dto.response.LoginResponse;
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
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        OAuth2UserImpl oAuth2User = (OAuth2UserImpl) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(oAuth2User.getUserId(), oAuth2User.getEmail(),
            oAuth2User.getAuthorities());
        String refreshToken = jwtService.generateRefreshToken(oAuth2User.getUserId());

        //TODO: RT 저장소에 저장
        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtil.writeValueAsString(loginResponse));
    }
}
