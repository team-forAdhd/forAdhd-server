package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.AppleIdTokenValidator;
import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.auth.business.service.OAuth2UserAttributesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.ID_TOKEN;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppleOAuth2UserAttributesServiceImpl implements OAuth2UserAttributesService {

    private final AppleIdTokenValidator appleIdTokenValidator;
    private final JwtService jwtService;

    @Override
    public Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        String idToken = oAuth2UserRequest.getAdditionalParameters().get(ID_TOKEN).toString();
        appleIdTokenValidator.validate(idToken);

        Map<String, Object> decodedPayload = jwtService.decodePayload(idToken);
        attributes.putAll(decodedPayload);
        return attributes;
    }
}
