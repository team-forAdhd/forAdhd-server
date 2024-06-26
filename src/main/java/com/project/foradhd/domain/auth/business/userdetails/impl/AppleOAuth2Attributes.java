package com.project.foradhd.domain.auth.business.userdetails.impl;

import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;

import java.time.LocalDate;
import java.util.Map;

import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleOAuth2Attributes extends OAuth2Attributes {

    private static final String ID_KEY = "sub";
    private static final String EMAIL_VERIFIED_KEY = "email_verified";
    private static final String EMAIL_KEY = "email";

    public static OAuth2Attributes of(String nameAttributesKey, Map<String, Object> attributes) {
        return GoogleOAuth2Attributes.builder()
                .id(parseId(attributes))
                .name("")
                .email(parseEmail(attributes))
                .isVerifiedEmail(parseIsVerifiedEmail(attributes))
                .gender(Gender.UNKNOWN)
                .ageRange("")
                .birth(LocalDate.now())
                .provider(Provider.APPLE)
                .build();
    }

    private static String parseId(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(ID_KEY));
    }

    private static String parseEmail(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(EMAIL_KEY));
    }

    private static Boolean parseIsVerifiedEmail(Map<String, Object> attributes) {
        return (boolean) attributes.get(EMAIL_VERIFIED_KEY);
    }
}
