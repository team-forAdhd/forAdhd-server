package com.project.foradhd.domain.auth.business.userdetails.client.fallbackfactory;

import com.project.foradhd.domain.auth.business.userdetails.client.GoogleOAuth2PeopleClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class GoogleOAuth2PeopleClientFallbackFactory implements FallbackFactory<GoogleOAuth2PeopleClient> {

    @Override
    public GoogleOAuth2PeopleClient create(Throwable cause) {
        log.error("An exception occurred when calling the GoogleOAuth2PeopleClient", cause);

        //Google People API 호출 시 에러 발생하면 기본 응답값을 반환하는 client 정의
        return new GoogleOAuth2PeopleClient() {

            @Override
            public Map<String, Object> getUserInfo(String authorization) {
                return Map.of();
            }
        };
    }
}
