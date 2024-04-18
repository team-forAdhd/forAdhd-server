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

    @Override
    public void validatePasswordMatches(String userId, String password) {
        AuthPassword authPassword = getAuthPassword(userId);
        if (!passwordEncoder.matches(password, authPassword.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    @Override
    public void updatePassword(String userId, String newPassword) {
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        AuthPassword authPassword = getAuthPassword(userId);
        authPassword.updateEncodedPassword(encodedNewPassword);
    }

    private AuthPassword getAuthPassword(String userId) {
        return authPasswordRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("일반 회원가입 유저가 아닙니다."));
    }
}
