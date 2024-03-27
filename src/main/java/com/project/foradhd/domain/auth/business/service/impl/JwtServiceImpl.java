package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.Provider;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtServiceImpl implements TokenService {

    private static final String EMAIL_CLAIM_NAME = "email";
    private static final String PROVIDER_CLAIM_NAME = "provider";
    private final Long accessTokenExpiry;
    private final Long refreshTokenExpiry;
    private final Key key;

    public JwtServiceImpl(@Value("${jwt.expiry.access-token}") Long accessTokenExpiry,
        @Value("${jwt.expiry.refresh-token}") Long refreshTokenExpiry,
        @Value("${jwt.secret-key}") String secretKey) {
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Override
    public String generateAccessToken(String userId, String email, Provider provider) {
        Date now = new Date();
        return Jwts.builder()
            .setSubject(userId)
            .addClaims(Map.of(EMAIL_CLAIM_NAME, email,
                PROVIDER_CLAIM_NAME, provider))
            .setIssuedAt(now)
            .setExpiration(calculateExpiration(now, accessTokenExpiry))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public String generateRefreshToken(String userId) {
        Date now = new Date();
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(now)
            .setExpiration(calculateExpiration(now, refreshTokenExpiry))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public void validateTokenExpiry(String token) {
        try {
            parseJwt(token);
        } catch (UnsupportedJwtException e) {
            log.error("The claimsJwt argument does not represent an unsigned Claims JWT");
            throw e;
        } catch (MalformedJwtException e) {
            log.error("The claimsJwt string is not a valid JWT");
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("The specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked.");
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("The claimsJwt string is null or empty or only whitespace");
            throw new JwtException(e.getMessage(), e);
        }
    }

    @Override
    public void validateTokenForm(String token) {
        try {
            parseJwt(token);
        } catch (UnsupportedJwtException e) {
            log.error("The claimsJwt argument does not represent an unsigned Claims JWT");
            throw e;
        } catch (MalformedJwtException e) {
            log.error("The claimsJwt string is not a valid JWT");
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("The claimsJwt string is null or empty or only whitespace");
            throw new JwtException(e.getMessage(), e);
        }
    }

    @Override
    public String getSubject(String token) {
        Claims claims = parseJwt(token);
        return claims.getSubject();
    }

    private Date calculateExpiration(Date now, Long expiry) {
        Instant expirationInstant = now.toInstant().plusMillis(expiry);
        return Date.from(expirationInstant);
    }

    private Claims parseJwt(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJwt(token)
            .getBody();
    }
}
