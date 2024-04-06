package com.project.foradhd.domain.auth.business.userdetails.impl;

import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import java.time.LocalDate;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoOAuth2Attributes extends OAuth2Attributes {

    private static final String ID_KEY = "id";
    private static final String ACCOUNT_KEY = "kakao_account";
    private static final String PROFILE_KEY = "profile";
    private static final String NAME_KEY = "nickname";
    private static final String EMAIL_VALID_KEY = "is_email_valid";
    private static final String EMAIL_VERIFIED_KEY = "is_email_verified";
    private static final String EMAIL_KEY = "email";
    //비즈앱 등록 후 접근 가능
    private static final String GENDER_KEY = "gender";
    private static final String AGE_RANGE_KEY = "age_range";
    private static final String BIRTH_DAY_KEY = "birthday";
    private static final String BIRTH_YEAR_KEY = "birthyear";

    public static OAuth2Attributes of(String nameAttributesKey, Map<String, Object> attributes) {
        Map<String, Object> userInfo = (Map<String, Object>) attributes.get(ACCOUNT_KEY);
        Map<String, Object> profile = (Map<String, Object>) userInfo.get(PROFILE_KEY);
        return KakaoOAuth2Attributes.builder()
            .id(parseId(attributes))
            .name(parseName(profile))
            .email(parseEmail(userInfo))
            .gender(Gender.UNKNOWN)
            .ageRange("")
            .birth(LocalDate.now())
            .provider(Provider.KAKAO)
            .build();
    }

    private static String parseId(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(ID_KEY));
    }

    private static String parseName(Map<String, Object> profile) {
        return (String) profile.get(NAME_KEY);
    }

    private static String parseEmail(Map<String, Object> userInfo) {
        Boolean isEmailValid = (Boolean) userInfo.get(EMAIL_VALID_KEY);
        Boolean isEmailVerified = (Boolean) userInfo.get(EMAIL_VERIFIED_KEY);
        if (isEmailValid && isEmailVerified) {
            return (String) userInfo.get(EMAIL_KEY);
        }
        return "";
    }
}
