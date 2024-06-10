package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.OAuth2UserAttributesService;
import com.project.foradhd.domain.auth.business.userdetails.client.GoogleOAuth2PeopleApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GoogleOAuth2UserAttributesServiceImpl implements OAuth2UserAttributesService {

    private static final String PERSON_FIELDS_PARAM = "birthdays,ageRanges,genders";
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

    private final GoogleOAuth2PeopleApiClient googleOAuth2PeopleApiClient;

    @Override
    public Map<String, Object> getAttributes(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        String accessToken = oAuth2UserRequest.getAccessToken().getTokenValue();
        ResponseEntity<Map<String, Object>> response = googleOAuth2PeopleApiClient.getUserInfo(PERSON_FIELDS_PARAM,
                AUTHORIZATION_HEADER_PREFIX + accessToken);
        if (response.getStatusCode().is2xxSuccessful()) {
            attributes = new HashMap<>(attributes);

            Map<String, Object> body = response.getBody();
            attributes.put(BIRTH_SET_KEY, parseBirth(body));
            attributes.put(AGE_RANGE_SET_KEY, parseAgeRange(body));
            attributes.put(GENDER_SET_KEY, parseGender(body));
        }
        return attributes;
    }

    private LocalDate parseBirth(Map<String, Object> body) {
        if (body.containsKey(BIRTHDAYS_GET_KEY)) {
            ArrayList<Map<String, Object>> birthdays = (ArrayList) body.get(BIRTHDAYS_GET_KEY);
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

    private String parseAgeRange(Map<String, Object> body) {
        if (body.containsKey(AGE_RANGES_GET_KEY)) {
            ArrayList<Map<String, Object>> ageRanges = (ArrayList) body.get(AGE_RANGES_GET_KEY);
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

    private String parseGender(Map<String, Object> body) {
        if (body.containsKey(GENDERS_GET_KEY)) {
            ArrayList<Map<String, Object>> genders = (ArrayList) body.get(GENDERS_GET_KEY);
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
