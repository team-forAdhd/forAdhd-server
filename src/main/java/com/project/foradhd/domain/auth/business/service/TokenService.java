package com.project.foradhd.domain.auth.business.service;

import java.security.Provider;

public interface TokenService {

    String generateAccessToken(String userId, String email, Provider provider);
    String generateRefreshToken(String userId);
    void validateTokenExpiry(String token);
    void validateTokenForm(String token);
    String getSubject(String token);
}
