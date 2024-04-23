package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.out.SignUpTokenData;
import com.project.foradhd.domain.user.persistence.entity.User;

public interface UserSignUpTokenService {

    SignUpTokenData generateSignUpToken(User user);
}
