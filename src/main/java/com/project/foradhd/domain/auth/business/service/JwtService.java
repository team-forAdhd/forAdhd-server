package com.project.foradhd.domain.auth.business.service;

import com.project.foradhd.domain.user.persistence.enums.Provider;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface JwtService {

    String generateAccessToken(String userId, String email, Provider provider, Collection<GrantedAuthority> authorities);
    String generateRefreshToken(String userId);
    void validateTokenExpiry(String token);
    void validateTokenForm(String token);
    boolean isValidTokenExpiry(String token);
    boolean isValidTokenForm(String token);
    String getSubject(String token);
    Collection<GrantedAuthority> getAuthorities(String token);
}
