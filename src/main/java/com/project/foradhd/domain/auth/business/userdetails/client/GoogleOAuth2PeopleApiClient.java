package com.project.foradhd.domain.auth.business.userdetails.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "googleOAuth2PeopleApiClient", url = "https://people.googleapis.com/v1/people/me")
public interface GoogleOAuth2PeopleApiClient {

    @GetMapping
    ResponseEntity<Map<String, Object>> getUserInfo(@RequestParam("personFields") String personFields,
                                    @RequestHeader("Authorization") String authorization);
}
