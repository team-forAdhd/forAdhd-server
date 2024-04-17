package com.project.foradhd.domain.auth.web.dto.request;

import lombok.Getter;

@Getter
public class TokenReissueRequest {

    private String accessToken;

    private String refreshToken;
}
