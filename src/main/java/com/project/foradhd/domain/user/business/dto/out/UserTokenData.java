package com.project.foradhd.domain.user.business.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenData {

    private String accessToken;
    private String refreshToken;
}
