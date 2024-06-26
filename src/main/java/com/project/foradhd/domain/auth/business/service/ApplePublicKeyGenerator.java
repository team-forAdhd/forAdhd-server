package com.project.foradhd.domain.auth.business.service;

import com.project.foradhd.domain.auth.web.dto.response.ApplePublicKeyListResponse;
import com.project.foradhd.domain.auth.web.dto.response.ApplePublicKeyListResponse.ApplePublicKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ApplePublicKeyGenerator {

    private static final String KID_HEADER = "kid";
    private static final String ALG_HEADER = "alg";

    public PublicKey generatePublicKey(Map<String, Object> headers,
                                    ApplePublicKeyListResponse applePublicKeyList) {
        String kid = (String) headers.get(KID_HEADER);
        String alg = (String) headers.get(ALG_HEADER);
        ApplePublicKeyResponse applePublicKey = getMatchedPublicKey(kid, alg, applePublicKeyList);
        return getPublicKey(applePublicKey);
    }

    private ApplePublicKeyResponse getMatchedPublicKey(String kid, String alg, ApplePublicKeyListResponse applePublicKeyList) {
        return applePublicKeyList.keys().stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("Invalid Apple Public Keys"));
    }

    private PublicKey getPublicKey(ApplePublicKeyResponse applePublicKey) {
        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.e());

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.kty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
