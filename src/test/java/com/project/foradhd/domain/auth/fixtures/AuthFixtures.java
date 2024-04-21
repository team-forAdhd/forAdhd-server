package com.project.foradhd.domain.auth.fixtures;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.auth.persistence.entity.AuthPassword.AuthPasswordBuilder;

public class AuthFixtures {

    public static AuthPasswordBuilder toAuthPassword() {
        return AuthPassword.builder()
            .user(toUser().build())
            .password("password");
    }
}
