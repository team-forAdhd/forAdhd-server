package com.project.foradhd.domain.auth.business.userdetails;

import com.project.foradhd.domain.auth.business.userdetails.impl.*;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.global.exception.BusinessException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

import static com.project.foradhd.global.exception.ErrorCode.NOT_SUPPORTED_SNS_TYPE;

public abstract class OAuth2AttributesFactory {

    public static OAuth2Attributes valueOf(OAuth2UserRequest oAuth2UserRequest, String nameAttributesKey,
                                        Map<String, Object> attributes) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.from(registrationId)
            .orElseThrow(() -> new BusinessException(NOT_SUPPORTED_SNS_TYPE));
        switch (provider) {
            case NAVER -> {
                return NaverOAuth2Attributes.of(nameAttributesKey, attributes);
            }
            case KAKAO -> {
                return KakaoOAuth2Attributes.of(nameAttributesKey, attributes);
            }
            case GOOGLE -> {
                String accessToken = oAuth2UserRequest.getAccessToken().getTokenValue();
                return GoogleOAuth2Attributes.of(nameAttributesKey, attributes, accessToken);
            }
            case APPLE -> {
                return AppleOAuth2Attributes.of(nameAttributesKey, attributes);
            }
            default -> throw new BusinessException(NOT_SUPPORTED_SNS_TYPE);
        }
    }
}
