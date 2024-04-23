package com.project.foradhd.domain.auth.fixtures;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.auth.persistence.entity.AuthPassword.AuthPasswordBuilder;
import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin;
import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin.AuthSocialLoginBuilder;
import com.project.foradhd.domain.user.persistence.enums.Provider;

public class AuthFixtures {

    public static AuthPasswordBuilder toAuthPassword() {
        return AuthPassword.builder()
            .id("authPasswordId")
            .user(toUser().build())
            .password("password");
    }

    public static AuthSocialLoginBuilder toAuthSocialLogin() {
        return AuthSocialLogin.builder()
            .id("authSocialLoginId")
            .user(toUser().build())
            .externalUserId("externalUserId")
            .provider(Provider.NAVER);
    }
}
