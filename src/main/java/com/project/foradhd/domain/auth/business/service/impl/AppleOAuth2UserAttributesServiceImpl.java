package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.OAuth2UserAttributesService;
import com.project.foradhd.global.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.ID_TOKEN;

@Slf4j
@Service
public class AppleOAuth2UserAttributesServiceImpl implements OAuth2UserAttributesService {

    @Override
    public Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        String idToken = oAuth2UserRequest.getAdditionalParameters().get(ID_TOKEN).toString();
        attributes.putAll(decodeIdToken(idToken));
        return attributes;
    }

    public Map<String, Object> decodeIdToken(String idToken) {
        String[] encodedPayload = idToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        byte[] decodedBytes = decoder.decode(encodedPayload[1].getBytes(StandardCharsets.UTF_8));
        String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);
        return JsonUtil.readValue(decodedPayload, Map.class);
    }
}
