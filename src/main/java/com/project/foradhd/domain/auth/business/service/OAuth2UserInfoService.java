package com.project.foradhd.domain.auth.business.service;

import com.project.foradhd.domain.auth.business.service.impl.AppleOAuth2UserAttributesServiceImpl;
import com.project.foradhd.domain.auth.business.service.impl.GoogleOAuth2UserAttributesServiceImpl;
import com.project.foradhd.domain.auth.business.service.impl.KakaoOAuth2UserAttributesServiceImpl;
import com.project.foradhd.domain.auth.business.service.impl.NaverOAuth2UserAttributesServiceImpl;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.project.foradhd.global.exception.ErrorCode.NOT_SUPPORTED_SNS_TYPE;

@RequiredArgsConstructor
@Service
public class OAuth2UserInfoService {

    private final NaverOAuth2UserAttributesServiceImpl naverOAuth2UserAttributesService;
    private final KakaoOAuth2UserAttributesServiceImpl kakaoOAuth2UserAttributesService;
    private final GoogleOAuth2UserAttributesServiceImpl googleOAuth2UserAttributesService;
    private final AppleOAuth2UserAttributesServiceImpl appleOAuth2UserAttributesService;

    public final Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.from(registrationId)
                .orElseThrow(() -> new BusinessException(NOT_SUPPORTED_SNS_TYPE));
        Map<String, Object> attributes = oAuth2User.getAttributes();
        switch (provider) {
            case NAVER -> {
                return naverOAuth2UserAttributesService.getAttributes(oAuth2UserRequest, attributes);
            }
            case KAKAO -> {
                return kakaoOAuth2UserAttributesService.getAttributes(oAuth2UserRequest, attributes);
            }
            case GOOGLE -> {
                return googleOAuth2UserAttributesService.getAttributes(oAuth2UserRequest, attributes);
            }
            case APPLE -> {
                return appleOAuth2UserAttributesService.getAttributes(oAuth2UserRequest, attributes);
            }
            default -> throw new BusinessException(NOT_SUPPORTED_SNS_TYPE);
        }
    }
}
