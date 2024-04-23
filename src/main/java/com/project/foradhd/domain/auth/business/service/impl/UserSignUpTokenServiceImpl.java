package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.user.business.dto.out.SignUpTokenData;
import com.project.foradhd.domain.user.business.service.UserSignUpTokenService;
import com.project.foradhd.domain.user.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserSignUpTokenServiceImpl implements UserSignUpTokenService {

    private final JwtService jwtService;

    @Override
    public SignUpTokenData generateSignUpToken(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(),
            AuthorityUtils.createAuthorityList(user.getAuthority()));
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        //TODO: RT 저장소에 저장
        return new SignUpTokenData(accessToken, refreshToken);
    }
}
