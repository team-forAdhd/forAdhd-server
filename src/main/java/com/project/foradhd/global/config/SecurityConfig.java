package com.project.foradhd.global.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

import com.project.foradhd.domain.auth.filter.JwtAuthenticationFilter;
import com.project.foradhd.domain.auth.filter.LoginAuthenticationFilter;
import com.project.foradhd.domain.auth.handler.JwtAuthenticationFailureHandler;
import com.project.foradhd.domain.auth.handler.JwtAuthorizationFailureHandler;
import com.project.foradhd.domain.auth.handler.JwtLogoutSuccessHandler;
import com.project.foradhd.domain.auth.handler.LoginAuthenticationFailureHandler;
import com.project.foradhd.domain.auth.handler.LoginAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final String SIGN_UP_API_PATH = "/api/v1/user/sign-up";
    private static final String EMAIL_AUTH_API_PATH = "/api/v1/user/email-auth";
    private static final String LOGIN_API_PATH = "/api/v1/auth/login";
    private static final String LOGOUT_API_PATH = "/api/v1/auth/logout";
    private static final String HEALTH_CHECK_API_PATH = "/api/v1/health-check";
    private static final RequestMatcher loginMatcher = new AntPathRequestMatcher(LOGIN_API_PATH, POST.name());
    private static final RequestMatcher logoutMatcher = new AntPathRequestMatcher(LOGOUT_API_PATH, DELETE.name());

    private final LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;
    private final LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final JwtAuthorizationFailureHandler jwtAuthorizationFailureHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(registry -> registry
                .requestMatchers(SIGN_UP_API_PATH, EMAIL_AUTH_API_PATH,
                    LOGIN_API_PATH, LOGOUT_API_PATH, HEALTH_CHECK_API_PATH).permitAll()
                .anyRequest().authenticated())
            .sessionManagement(config -> config
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(CsrfConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .formLogin(FormLoginConfigurer::disable)
            .logout(config -> config
                .logoutRequestMatcher(logoutMatcher).permitAll()
                .clearAuthentication(true)
                .logoutSuccessHandler(jwtLogoutSuccessHandler))
            .exceptionHandling(config -> config
                .authenticationEntryPoint(jwtAuthenticationFailureHandler)
                .accessDeniedHandler(jwtAuthorizationFailureHandler))
            .addFilter(loginAuthenticationFilter(authenticationManager(authenticationConfiguration)))
            .addFilterAfter(jwtAuthenticationFilter, LoginAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter(AuthenticationManager authenticationManager) {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();
        loginAuthenticationFilter.setRequiresAuthenticationRequestMatcher(loginMatcher);
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        loginAuthenticationFilter.setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler);
        loginAuthenticationFilter.setAuthenticationFailureHandler(loginAuthenticationFailureHandler);
        return loginAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
