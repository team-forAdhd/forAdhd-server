package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.out.UserTokenData;
import com.project.foradhd.domain.user.persistence.entity.User;

public interface UserTokenService {

    UserTokenData generateToken(User user);
}
