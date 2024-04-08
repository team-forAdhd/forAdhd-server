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
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverOAuth2Attributes extends OAuth2Attributes {

    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String EMAIL_KEY = "email";
    private static final String GENDER_KEY = "gender";
    private static final String AGE_RANGE_KEY = "age";
    private static final String BIRTH_DAY_KEY = "birthday";
    private static final String BIRTH_YEAR_KEY = "birthyear";
    private static final DateTimeFormatter BIRTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    public static OAuth2Attributes of(String nameAttributesKey, Map<String, Object> attributes) {
        Map<String, Object> userInfo = (Map<String, Object>) attributes.get(nameAttributesKey);
        return NaverOAuth2Attributes.builder()
            .id(parseId(userInfo))
            .name(parseName(userInfo))
            .email(parseEmail(userInfo))
            .isVerifiedEmail(true)
            .gender(parseGender(userInfo))
            .ageRange(parseAgeRange(userInfo))
            .birth(parseBirth(userInfo))
            .provider(Provider.NAVER)
            .build();
    }

    private static String parseId(Map<String, Object> userInfo) {
        return (String) userInfo.get(ID_KEY);
    }

    private static String parseName(Map<String, Object> userInfo) {
        return (String) userInfo.get(NAME_KEY);
    }

    private static String parseEmail(Map<String, Object> userInfo) {
        return (String) userInfo.get(EMAIL_KEY);
    }

    private static Gender parseGender(Map<String, Object> userInfo) {
        String gender = (String) userInfo.get(GENDER_KEY);
        return Gender.from(gender);
    }

    private static String parseAgeRange(Map<String, Object> userInfo) {
        return (String) userInfo.get(AGE_RANGE_KEY);
    }

    private static LocalDate parseBirth(Map<String, Object> userInfo) {
        int birthYear = Integer.parseInt((String) userInfo.get(BIRTH_YEAR_KEY));
        String birthDay = (String) userInfo.get(BIRTH_DAY_KEY);
        TemporalAccessor birthDayTemporalAccessor = BIRTH_DAY_FORMATTER.parse(birthDay);
        return MonthDay.from(birthDayTemporalAccessor).atYear(birthYear);
    }
}
