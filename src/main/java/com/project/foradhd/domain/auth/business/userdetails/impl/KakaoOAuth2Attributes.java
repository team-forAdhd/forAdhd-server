package com.project.foradhd.domain.auth.business.userdetails.impl;

import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoOAuth2Attributes extends OAuth2Attributes {

    public static OAuth2Attributes of(String nameAttributesKey, Map<String, Object> attributes) {
        return null;
    }
}
