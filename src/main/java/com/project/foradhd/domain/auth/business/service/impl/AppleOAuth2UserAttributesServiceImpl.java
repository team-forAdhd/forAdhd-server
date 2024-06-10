package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.OAuth2UserAttributesService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AppleOAuth2UserAttributesServiceImpl implements OAuth2UserAttributesService {

    @Override
    public Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        return null;
    }
}
