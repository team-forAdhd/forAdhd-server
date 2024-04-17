package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.persistence.entity.User;

public interface UserAuthInfoService {

    void signUpByPassword(User user, String password);
}
