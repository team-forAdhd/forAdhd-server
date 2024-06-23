package com.project.foradhd.domain.auth.business.service;

import java.security.Key;
import java.util.Collection;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

public interface JwtService {

    String generateAccessToken(String userId, String email, Collection<GrantedAuthority> authorities);
    String generateRefreshToken(String userId);
    void validateTokenExpiry(String token);
    void validateTokenExpiry(String token, Key key);
    void validateTokenForm(String token);
    boolean isValidTokenExpiry(String token);
    boolean isValidTokenForm(String token);
    String getSubject(String token);
    Collection<GrantedAuthority> getAuthorities(String token);
    Map<String, Object> decodeHeader(String token);
    Map<String, Object> decodePayload(String token);
    Claims parseToken(String token, Key key);
    Claims parseExpiredToken(String token, Key key);
    void saveRefreshToken(String userId, String refreshToken);
    void deleteRefreshToken(String userId);
    boolean existsSavedRefreshToken(String userId, String refreshToken);
}
