package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.OAuth2UserAttributesService;
import com.project.foradhd.domain.auth.business.userdetails.client.GoogleOAuth2PeopleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GoogleOAuth2UserAttributesServiceImpl implements OAuth2UserAttributesService {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private static final String DEFAULT_GENDER_VALUE = "unspecified";

    private static final String METADATA_GET_KEY = "metadata";
    private static final String PRIMARY_GET_KEY = "primary";

    private static final String BIRTHDAYS_GET_KEY = "birthdays";
    private static final String BIRTH_DATE_GET_KEY = "date";
    private static final String BIRTH_YEAR_GET_KEY = "year";
    private static final String BIRTH_MONTH_GET_KEY = "month";
    private static final String BIRTH_DAY_GET_KEY = "day";

    private static final String AGE_RANGES_GET_KEY = "ageRanges";
    private static final String AGE_RANGE_GET_KEY = "ageRange";

    private static final String GENDERS_GET_KEY = "genders";
    private static final String GENDER_GET_KEY = "value";

    private static final String BIRTH_SET_KEY = "birth";
    private static final String AGE_RANGE_SET_KEY = "age_range";
    private static final String GENDER_SET_KEY = "gender";

    private final GoogleOAuth2PeopleClient googleOAuth2PeopleClient;

    @Override
    public Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        String accessToken = oAuth2UserRequest.getAccessToken().getTokenValue();
        Map<String, Object> response = googleOAuth2PeopleClient.getUserInfo(AUTHORIZATION_HEADER_PREFIX + accessToken);

        attributes = new HashMap<>(attributes); //기존 타입 java.util.Collections$UnmodifiableMap → 변경 가능한 타입으로 대체
        attributes.put(BIRTH_SET_KEY, parseBirth(response));
        attributes.put(AGE_RANGE_SET_KEY, parseAgeRange(response));
        attributes.put(GENDER_SET_KEY, parseGender(response));
        return attributes;
    }

    private LocalDate parseBirth(Map<String, Object> attributes) {
        if (attributes.containsKey(BIRTHDAYS_GET_KEY)) {
            ArrayList<Map<String, Object>> birthdays = (ArrayList) attributes.get(BIRTHDAYS_GET_KEY);
            return birthdays.stream()
                    .filter(this::isPrimaryMetadata)
                    .filter(this::hasBirthData)
                    .findFirst()
                    .map(birthday -> {
                        Map<String, Object> birthDate = (Map<String, Object>) birthday.get(BIRTH_DATE_GET_KEY);
                        int year = (int) birthDate.get(BIRTH_YEAR_GET_KEY);
                        int month = (int) birthDate.get(BIRTH_MONTH_GET_KEY);
                        int day = (int) birthDate.get(BIRTH_DAY_GET_KEY);
                        return LocalDate.of(year, month, day);
                    })
                    .orElse(LocalDate.now());
        }
        return LocalDate.now();
    }

    private boolean hasBirthData(Map<String, Object> birthday) {
        if (!birthday.containsKey(BIRTH_DATE_GET_KEY)) return false;
        Map<String, Object> birthDate = (Map<String, Object>) birthday.get(BIRTH_DATE_GET_KEY);
        return  birthDate.containsKey(BIRTH_YEAR_GET_KEY) && birthDate.containsKey(BIRTH_MONTH_GET_KEY) &&
                birthDate.containsKey(BIRTH_DAY_GET_KEY);
    }

    private String parseAgeRange(Map<String, Object> attributes) {
        if (attributes.containsKey(AGE_RANGES_GET_KEY)) {
            ArrayList<Map<String, Object>> ageRanges = (ArrayList) attributes.get(AGE_RANGES_GET_KEY);
            return ageRanges.stream()
                    .filter(this::isPrimaryMetadata)
                    .filter(this::hasAgeRangeData)
                    .findFirst()
                    .map(ageRage -> (String) ageRage.get(AGE_RANGE_GET_KEY))
                    .orElse("");
        }
        return "";
    }

    private boolean hasAgeRangeData(Map<String, Object> ageRange) {
        return ageRange.containsKey(AGE_RANGE_GET_KEY);
    }

    private String parseGender(Map<String, Object> attributes) {
        if (attributes.containsKey(GENDERS_GET_KEY)) {
            ArrayList<Map<String, Object>> genders = (ArrayList) attributes.get(GENDERS_GET_KEY);
            return genders.stream()
                    .filter(this::isPrimaryMetadata)
                    .filter(this::hasGenderData)
                    .findFirst()
                    .map(gender -> (String) gender.get(GENDER_GET_KEY))
                    .orElse(DEFAULT_GENDER_VALUE);
        }
        return DEFAULT_GENDER_VALUE;
    }

    private boolean hasGenderData(Map<String, Object> gender) {
        return gender.containsKey(GENDER_GET_KEY);
    }

    private boolean isPrimaryMetadata(Map<String, Object> data) {
        if (!data.containsKey(METADATA_GET_KEY)) return false;
        Map<String, Object> metadata = (Map<String, Object>) data.get(METADATA_GET_KEY);
        if (!metadata.containsKey(PRIMARY_GET_KEY)) return false;
        return (boolean) metadata.get(PRIMARY_GET_KEY);
    }
}
