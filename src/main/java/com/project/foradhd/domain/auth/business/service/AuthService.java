package com.project.foradhd.domain.auth.business.service;

import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;

public interface AuthService {

    AuthTokenData reissue(String accessToken, String refreshToken);
}
