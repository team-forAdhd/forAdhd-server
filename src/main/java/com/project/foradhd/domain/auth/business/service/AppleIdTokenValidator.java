package com.project.foradhd.domain.auth.business.service;

import com.project.foradhd.domain.auth.web.client.ApplePublicKeyClient;
import com.project.foradhd.domain.auth.web.dto.response.ApplePublicKeyListResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppleIdTokenValidator {

    public static final String NONCE_SUPPORTED_CLAIM_KEY = "nonce_supported";
    private final ApplePublicKeyClient applePublicKeyClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final JwtService jwtService;

    @Value("${apple.url}")
    private String APPLE_URL;

    @Value("${apple.client-id}")
    private String APPLE_CLIENT_ID;

    //참고: https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user/
    public void validate(String idToken) {
        Map<String, Object> decodedHeader = jwtService.decodeHeader(idToken);
        DefaultJwsHeader jwtHeader = new DefaultJwsHeader(decodedHeader);
        PublicKey publicKey = getApplePublicKey(decodedHeader);

        //Verify the JWS E256 signature using the server’s public key
        //Verify that the time is earlier than the exp value of the token
        jwtService.validateTokenExpiry(idToken, publicKey);
        Claims claims = jwtService.parseToken(idToken, publicKey);

        //Verify the nonce for the authentication → "nonce_supported": true
        Boolean nonceSupported = claims.get(NONCE_SUPPORTED_CLAIM_KEY, Boolean.class);
        if (!nonceSupported) {
            throw new IncorrectClaimException(jwtHeader, claims,
                    "Invalid Apple Id Token: nonce_supported = " + nonceSupported);
        }

        //Verify that the iss field contains https://appleid.apple.com → "iss": "https://appleid.apple.com"
        String issuer = claims.getIssuer();
        if (!issuer.equals(APPLE_URL)) {
            throw new IncorrectClaimException(jwtHeader, claims, "Invalid Apple Id Token: issuer = " + issuer);
        }

        //Verify that the aud field is the developer’s client_id → "aud": ${APPLE_CLIENT_ID}
        String audience = claims.getAudience();
        if (!audience.equals(APPLE_CLIENT_ID)) {
            throw new IncorrectClaimException(jwtHeader, claims, "Invalid Apple Id Token: audience = " + audience);
        }
    }

    //참고: https://developer.apple.com/documentation/sign_in_with_apple/fetch_apple_s_public_key_for_verifying_token_signature
    private PublicKey getApplePublicKey(Map<String, Object> headers) {
        ApplePublicKeyListResponse applePublicKeyListResponse = applePublicKeyClient.getApplePublicKeyList();
        return applePublicKeyGenerator.generatePublicKey(headers, applePublicKeyListResponse);
    }
}
