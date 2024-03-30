package com.project.foradhd.domain.auth.filter;

import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.global.util.HeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final HeaderUtil headerUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String token = headerUtil.parseToken(request)
            .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("인증이 필요한 요청입니다."));
        if (!jwtService.isValidTokenExpiry(token)) {
            throw new AuthenticationCredentialsNotFoundException("인증이 필요한 요청입니다.");
        }

        String userId = jwtService.getSubject(token);
        Collection<GrantedAuthority> authorities = jwtService.getAuthorities(token);
        Authentication authentication = UsernamePasswordAuthenticationToken
            .authenticated(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
