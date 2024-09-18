package com.project.foradhd.config;

import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.auth.business.service.impl.JwtServiceImpl;
import com.project.foradhd.domain.auth.converter.CustomOAuth2AuthorizationCodeGrantRequestEntityConverter;
import com.project.foradhd.domain.auth.filter.JwtAuthenticationFilter;
import com.project.foradhd.domain.auth.handler.*;
import com.project.foradhd.domain.auth.persistence.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.global.config.SecurityConfig;
import com.project.foradhd.global.service.CacheService;
import com.project.foradhd.global.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Import(SecurityConfig.class)
@TestConfiguration
public class SecurityTestConfig {

    @MockBean
    UserService userService;

    @MockBean
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    @MockBean
    CacheService cacheService;

    @Bean
    public JwtService jwtService(CacheService cacheService,
                                @Value("${jwt.expiry.access-token}") Long accessTokenExpiry,
                                @Value("${jwt.expiry.refresh-token}") Long refreshTokenExpiry,
                                @Value("${jwt.secret-key}") String secretKey) {
        return new JwtServiceImpl(cacheService, accessTokenExpiry, refreshTokenExpiry, secretKey);
    }

    @Bean
    public HeaderUtil headerUtil() {
        return new HeaderUtil();
    }

    @Bean
    public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler(UserService userService, JwtService jwtService) {
        return new LoginAuthenticationSuccessHandler(userService, jwtService);
    }

    @Bean
    public LoginAuthenticationFailureHandler loginAuthenticationFailureHandler() {
        return new LoginAuthenticationFailureHandler();
    }

    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler(JwtService jwtService, HeaderUtil headerUtil) {
        return new JwtLogoutSuccessHandler(jwtService, headerUtil);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, HeaderUtil headerUtil) {
        return new JwtAuthenticationFilter(jwtService, headerUtil);
    }

    @Bean
    public JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }

    @Bean
    public JwtAuthorizationFailureHandler jwtAuthorizationFailureHandler() {
        return new JwtAuthorizationFailureHandler();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(UserService userService, JwtService jwtService) {
        return new OAuth2AuthenticationSuccessHandler(userService, jwtService);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public CustomOAuth2AuthorizationCodeGrantRequestEntityConverter customOAuth2AuthorizationCodeGrantRequestEntityConverter() {
        return new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter();
    }
}
