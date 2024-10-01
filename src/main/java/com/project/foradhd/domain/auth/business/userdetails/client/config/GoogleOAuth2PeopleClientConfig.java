package com.project.foradhd.domain.auth.business.userdetails.client.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class GoogleOAuth2PeopleClientConfig {

    private static final List<String> PERSON_FIELDS_PARAM = List.of("birthdays", "ageRanges", "genders");

    @Bean
    public RequestInterceptor googleOAuth2PeopleClientRequestInterceptor() {
        return requestTemplate -> requestTemplate.query("personFields", PERSON_FIELDS_PARAM);
    }
}
