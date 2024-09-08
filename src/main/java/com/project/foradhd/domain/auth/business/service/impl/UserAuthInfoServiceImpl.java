package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.auth.persistence.repository.AuthPasswordRepository;
import com.project.foradhd.domain.auth.persistence.repository.AuthSocialLoginRepository;
import com.project.foradhd.domain.user.business.service.UserAuthInfoService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.foradhd.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.project.foradhd.global.exception.ErrorCode.NOT_MATCH_USERNAME_PASSWORD;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAuthInfoServiceImpl implements UserAuthInfoService {

    private final AuthSocialLoginRepository authSocialLoginRepository;
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

    @Override
    public void validatePasswordMatches(String userId, String password) {
        AuthPassword authPassword = getAuthPassword(userId);
        if (!passwordEncoder.matches(password, authPassword.getPassword())) {
            throw new BusinessException(NOT_MATCH_USERNAME_PASSWORD);
        }
    }

    @Transactional
    @Override
    public void updatePassword(String userId, String newPassword) {
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        AuthPassword authPassword = getAuthPassword(userId);
        authPassword.updateEncodedPassword(encodedNewPassword);
    }

    @Override
    public AuthPassword getAuthPassword(String userId) {
        return authPasswordRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
    }

    @Transactional
    @Override
    public void withdraw(String userId) {
        authSocialLoginRepository.deleteByUserId(userId);
        authPasswordRepository.deleteByUserId(userId);
    }
}
