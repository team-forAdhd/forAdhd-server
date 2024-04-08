package com.project.foradhd.domain.auth.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuth2LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Boolean isSignedUpUser;
}
