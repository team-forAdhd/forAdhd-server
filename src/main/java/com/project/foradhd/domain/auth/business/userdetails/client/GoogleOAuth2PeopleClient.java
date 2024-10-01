package com.project.foradhd.domain.auth.business.userdetails.client;

import com.project.foradhd.domain.auth.business.userdetails.client.config.GoogleOAuth2PeopleClientConfig;
import com.project.foradhd.domain.auth.business.userdetails.client.fallbackfactory.GoogleOAuth2PeopleClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "googleOAuth2PeopleApiClient", url = "${google.people.url}",
        configuration = GoogleOAuth2PeopleClientConfig.class, fallbackFactory = GoogleOAuth2PeopleClientFallbackFactory.class)
public interface GoogleOAuth2PeopleClient {

    @GetMapping
    Map<String, Object> getUserInfo(@RequestHeader("Authorization") String authorization);
}
