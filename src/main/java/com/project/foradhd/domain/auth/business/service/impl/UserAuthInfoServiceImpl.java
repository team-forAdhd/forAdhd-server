package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.auth.persistence.repository.AuthPasswordRepository;
import com.project.foradhd.domain.user.business.service.UserAuthInfoService;
import com.project.foradhd.domain.user.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAuthInfoServiceImpl implements UserAuthInfoService {

    private final AuthPasswordRepository authPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void signUpByPassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        AuthPassword authPassword = AuthPassword.builder()
            .user(user)
            .password(encodedPassword)
            .build();
        authPasswordRepository.save(authPassword);
    }
}
