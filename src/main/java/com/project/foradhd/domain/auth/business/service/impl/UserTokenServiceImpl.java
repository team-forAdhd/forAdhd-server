package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.user.business.dto.out.UserTokenData;
import com.project.foradhd.domain.user.business.service.UserTokenService;
import com.project.foradhd.domain.user.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserTokenServiceImpl implements UserTokenService {

    private final JwtService jwtService;

    @Override
    public UserTokenData generateToken(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(),
            AuthorityUtils.createAuthorityList(user.getAuthority()));
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        jwtService.saveRefreshToken(user.getId(), refreshToken);
        return new UserTokenData(accessToken, refreshToken);
    }
}
