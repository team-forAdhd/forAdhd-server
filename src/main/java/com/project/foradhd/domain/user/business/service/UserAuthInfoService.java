package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.user.persistence.entity.User;

public interface UserAuthInfoService {

    void signUpByPassword(User user, String password);

    void validatePasswordMatches(String userId, String password);

    void updatePassword(String userId, String newPassword);

    AuthPassword getAuthPassword(String userId);

    void withdraw(String userId);
}
