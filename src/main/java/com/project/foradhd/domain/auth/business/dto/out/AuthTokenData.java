package com.project.foradhd.domain.auth.business.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokenData {

    private String accessToken;

    private String refreshToken;
}
