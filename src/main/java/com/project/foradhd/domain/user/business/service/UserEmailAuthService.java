package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.EmailAuthData;
import com.project.foradhd.domain.user.business.dto.in.EmailAuthValidationData;
import com.project.foradhd.domain.user.persistence.entity.User;

public interface UserEmailAuthService {

    void authenticateEmail(String userId, EmailAuthData emailAuthData);

    User validateEmailAuth(String userId, EmailAuthValidationData emailAuthValidationData);
}
