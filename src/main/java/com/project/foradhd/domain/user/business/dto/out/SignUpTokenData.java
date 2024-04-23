package com.project.foradhd.domain.user.business.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpTokenData {

    private String accessToken;
    private String refreshToken;
}
