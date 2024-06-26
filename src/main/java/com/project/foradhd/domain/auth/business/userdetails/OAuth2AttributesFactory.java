package com.project.foradhd.domain.auth.business.userdetails;

import com.project.foradhd.domain.auth.business.userdetails.impl.*;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.global.exception.BusinessException;

import java.util.Map;

import static com.project.foradhd.global.exception.ErrorCode.NOT_SUPPORTED_SNS_TYPE;

public abstract class OAuth2AttributesFactory {

    public static OAuth2Attributes valueOf(String registrationId, String nameAttributesKey,
                                        Map<String, Object> attributes) {
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
                return GoogleOAuth2Attributes.of(nameAttributesKey, attributes);
            }
            case APPLE -> {
                return AppleOAuth2Attributes.of(nameAttributesKey, attributes);
            }
            default -> throw new BusinessException(NOT_SUPPORTED_SNS_TYPE);
        }
    }
}
