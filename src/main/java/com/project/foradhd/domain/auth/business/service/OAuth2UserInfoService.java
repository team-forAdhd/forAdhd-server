package com.project.foradhd.domain.auth.business.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.function.Function;

public interface OAuth2UserInfoService {

    Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest,
                                    Function<OAuth2UserRequest, OAuth2User> defaultOAuth2UserFunction);
}
