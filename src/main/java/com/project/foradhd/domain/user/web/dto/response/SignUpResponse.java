package com.project.foradhd.domain.user.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {

    private String accessToken;
    private String refreshToken;
    private Boolean hasVerifiedEmail;
}
