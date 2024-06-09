package com.project.foradhd.domain.auth.business.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

public interface OAuth2UserAttributesService {

    default Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        return attributes;
    }
}
