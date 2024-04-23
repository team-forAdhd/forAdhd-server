package com.project.foradhd.domain.user.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SnsSignUpResponse {

    private String accessToken;
    private String refreshToken;
    private Boolean hasVerifiedEmail;
    private Boolean hasProfile;
}
