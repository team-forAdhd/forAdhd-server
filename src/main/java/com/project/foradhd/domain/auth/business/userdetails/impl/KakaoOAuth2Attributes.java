package com.project.foradhd.domain.auth.business.userdetails.impl;

import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoOAuth2Attributes extends OAuth2Attributes {

    private static final String ID_KEY = "id";
    private static final String ACCOUNT_KEY = "kakao_account";
    private static final String NAME_KEY = "name";
    private static final String HAS_EMAIL_KEY = "has_email";
    private static final String EMAIL_VALID_KEY = "is_email_valid";
    private static final String EMAIL_VERIFIED_KEY = "is_email_verified";
    private static final String EMAIL_KEY = "email";
    private static final String HAS_GENDER_KEY = "has_gender";
    private static final String GENDER_KEY = "gender";
    private static final String HAS_AGE_RANGE_KEY = "has_age_range";
    private static final String AGE_RANGE_KEY = "age_range";
    private static final String HAS_BIRTH_YEAR_KEY = "has_birthyear";
    private static final String HAS_BIRTH_DAY_KEY = "has_birthday";
    private static final String BIRTH_YEAR_KEY = "birthyear";
    private static final String BIRTH_DAY_KEY = "birthday";
    private static final DateTimeFormatter BIRTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMdd");

    public static OAuth2Attributes of(String nameAttributesKey, Map<String, Object> attributes) {
        Map<String, Object> userInfo = (Map<String, Object>) attributes.get(ACCOUNT_KEY);
        return KakaoOAuth2Attributes.builder()
            .id(parseId(attributes))
            .name(parseName(userInfo))
            .email(parseEmail(userInfo))
            .isVerifiedEmail(parseIsVerifiedEmail(userInfo))
            .gender(parseGender(userInfo))
            .ageRange(parseAgeRange(userInfo))
            .birth(parseBirth(userInfo))
            .provider(Provider.KAKAO)
            .build();
    }

    private static String parseId(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(ID_KEY));
    }

    private static String parseName(Map<String, Object> userInfo) {
        return (String) userInfo.get(NAME_KEY);
    }

    private static String parseEmail(Map<String, Object> userInfo) {
        boolean hasEmail = (boolean) userInfo.get(HAS_EMAIL_KEY);
        if (!hasEmail)return "";
        return (String) userInfo.get(EMAIL_KEY);
    }

    private static Boolean parseIsVerifiedEmail(Map<String, Object> userInfo) {
        Boolean isEmailValid = (Boolean) userInfo.get(EMAIL_VALID_KEY);
        Boolean isEmailVerified = (Boolean) userInfo.get(EMAIL_VERIFIED_KEY);
        return isEmailValid && isEmailVerified;
    }

    private static Gender parseGender(Map<String, Object> userInfo) {
        boolean hasGender = (boolean) userInfo.get(HAS_GENDER_KEY);
        if (!hasGender) return Gender.UNKNOWN;

        String gender = (String) userInfo.get(GENDER_KEY);
        return KakaoGender.from(gender);
    }

    private static String parseAgeRange(Map<String, Object> userInfo) {
        boolean hasAgeRange = (boolean) userInfo.get(HAS_AGE_RANGE_KEY);
        if (!hasAgeRange) return "";
        return (String) userInfo.get(AGE_RANGE_KEY);
    }
    private static LocalDate parseBirth(Map<String, Object> userInfo) {
        boolean hasBirthYear = (boolean) userInfo.get(HAS_BIRTH_YEAR_KEY);
        boolean hasBirthDay = (boolean) userInfo.get(HAS_BIRTH_DAY_KEY);
        if (!hasBirthYear || !hasBirthDay) return LocalDate.now();

        int birthYear = Integer.parseInt((String) userInfo.get(BIRTH_YEAR_KEY));
        String birthDay = (String) userInfo.get(BIRTH_DAY_KEY);
        TemporalAccessor birthDayTemporalAccessor = BIRTH_DAY_FORMATTER.parse(birthDay);
        return MonthDay.from(birthDayTemporalAccessor).atYear(birthYear);
    }

    @Getter
    @RequiredArgsConstructor
    enum KakaoGender {

        MALE(Gender.MALE), FEMALE(Gender.FEMALE);

        private final Gender gender;

        public static Gender from(String value) {
            try {
                KakaoGender kakaoGender = KakaoGender.valueOf(value.toUpperCase());
                return kakaoGender.getGender();
            } catch (IllegalArgumentException e) {
                return Gender.UNKNOWN;
            }
        }
    }
}
