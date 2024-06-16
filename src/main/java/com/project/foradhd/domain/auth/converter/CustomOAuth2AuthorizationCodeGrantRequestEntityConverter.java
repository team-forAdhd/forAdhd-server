package com.project.foradhd.domain.auth.converter;

import com.project.foradhd.domain.user.persistence.enums.Provider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//Authorization Code 발급 응답 → Access Token 발급을 위한 요청 객체 생성하는 converter
@Slf4j
@Component
public class CustomOAuth2AuthorizationCodeGrantRequestEntityConverter
        implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private static final int CLIENT_SECRET_EXPIRY_MONTH = 6;

    @Value("${apple.url}")
    private String APPLE_URL;

    @Value("${apple.key-path}")
    private String APPLE_KEY_PATH;

    @Value("${apple.client-id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.team-id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.key-id}")
    private String APPLE_KEY_ID;

    private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

    public CustomOAuth2AuthorizationCodeGrantRequestEntityConverter() {
        this.defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest request) {
        RequestEntity<?> requestEntity = defaultConverter.convert(request);
        String registrationId = request.getClientRegistration().getRegistrationId();
        Optional<Provider> optionalProvider = Provider.from(registrationId);
        MultiValueMap<String, String> body = (MultiValueMap<String, String>) requestEntity.getBody();
        if (optionalProvider.isPresent() && optionalProvider.get() == Provider.APPLE) {
            body.set(OAuth2ParameterNames.CLIENT_SECRET, createClientSecret());
        }
        return new RequestEntity<>(body, requestEntity.getHeaders(), requestEntity.getMethod(), requestEntity.getUrl());
    }

    //참고: https://developer.apple.com/documentation/accountorganizationaldatasharing/creating-a-client-secret
    public String createClientSecret() {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiration = Date.from(now.atZone(ZoneOffset.UTC).plusMonths(CLIENT_SECRET_EXPIRY_MONTH).toInstant());
        Map<String, Object> header = new HashMap<>();
        header.put(DefaultJwsHeader.ALGORITHM, JwsAlgorithms.ES256);
        header.put(DefaultJwsHeader.KEY_ID, APPLE_KEY_ID);

        return Jwts.builder()
                .setHeaderParams(header)
                .setIssuer(APPLE_TEAM_ID)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .setAudience(APPLE_URL)
                .setSubject(APPLE_CLIENT_ID)
                .signWith(getPrivateKey())
                .compact();
    }

    public PrivateKey getPrivateKey() {
        ClassPathResource resource = new ClassPathResource(APPLE_KEY_PATH);
        try (InputStream inputStream = resource.getInputStream();
            PEMParser pemParser = new PEMParser(new StringReader(IOUtils.toString(inputStream, StandardCharsets.UTF_8)))) {
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPrivateKey(privateKeyInfo);
        } catch (IOException e) {
            log.error("Apple Key Parsing Error", e);
            throw new RuntimeException(e);
        }
    }
}
