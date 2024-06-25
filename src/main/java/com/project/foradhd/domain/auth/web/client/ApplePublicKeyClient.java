package com.project.foradhd.domain.auth.web.client;

import com.project.foradhd.domain.auth.web.dto.response.ApplePublicKeyListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "applePublicKeyClient", url = "${spring.security.oauth2.client.provider.apple.public-key-uri}")
public interface ApplePublicKeyClient {

    @GetMapping
    ApplePublicKeyListResponse getApplePublicKeyList();
}
