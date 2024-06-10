package com.project.foradhd.domain.auth.business.userdetails.impl;

import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

import static com.project.foradhd.domain.user.persistence.enums.Gender.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleOAuth2Attributes extends OAuth2Attributes {

    private static final String ID_KEY = "sub";
    private static final String NAME_KEY = "name";
    private static final String EMAIL_VERIFIED_KEY = "email_verified";
    private static final String EMAIL_KEY = "email";
    private static final String GENDER_KEY = "gender";
    private static final String AGE_RANGE_KEY = "age_range";
    private static final String BIRTH_KEY = "birth";

    public static OAuth2Attributes of(String nameAttributesKey, Map<String, Object> attributes) {
        return GoogleOAuth2Attributes.builder()
                .id(parseId(attributes))
                .name(parseName(attributes))
                .email(parseEmail(attributes))
                .isVerifiedEmail(parseIsVerifiedEmail(attributes))
                .gender(parseGender(attributes))
                .ageRange(parseAgeRange(attributes))
                .birth(parseBirth(attributes))
                .provider(Provider.GOOGLE)
                .build();
    }

    private static String parseId(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(ID_KEY));
    }

    private static String parseName(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(NAME_KEY));
    }

    private static String parseEmail(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(EMAIL_KEY));
    }

    private static Boolean parseIsVerifiedEmail(Map<String, Object> attributes) {
        return (boolean) attributes.get(EMAIL_VERIFIED_KEY);
    }

    private static Gender parseGender(Map<String, Object> attributes) {
        String gender = (String) attributes.get(GENDER_KEY);
        return GoogleGender.from(gender);
    }

    private static String parseAgeRange(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(AGE_RANGE_KEY));
    }

    private static LocalDate parseBirth(Map<String, Object> attributes) {
        return (LocalDate) attributes.get(BIRTH_KEY);
    }

    @Getter
    @RequiredArgsConstructor
    enum GoogleGender {

        MALE(Gender.MALE), FEMALE(Gender.FEMALE), UNSPECIFIED(Gender.UNKNOWN);

        private final Gender gender;

        public static Gender from(String value) {
            try {
                GoogleGender googleGender = GoogleGender.valueOf(value.toUpperCase());
                return googleGender.getGender();
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }
    }
}
