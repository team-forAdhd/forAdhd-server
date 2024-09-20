package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;
import com.project.foradhd.domain.auth.business.service.AuthService;
import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.project.foradhd.global.exception.ErrorCode.INVALID_AUTH_TOKEN;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public AuthTokenData reissue(String accessToken, String refreshToken) {
        validateAccessToken(accessToken);
        String userId = jwtService.getSubject(accessToken);
        validateRefreshToken(userId, refreshToken);

        User user = userService.getUser(userId);
        String reissuedAccessToken = jwtService.generateAccessToken(userId, user.getEmail(),
                createAuthorityList(user.getAuthority()));
        String reissuedRefreshToken = jwtService.generateRefreshToken(userId);
        jwtService.saveRefreshToken(userId, reissuedRefreshToken);
        return new AuthTokenData(reissuedAccessToken, reissuedRefreshToken);
    }

    private void validateAccessToken(String accessToken) {
        if (!jwtService.isValidTokenForm(accessToken)) {
            throw new BusinessException(INVALID_AUTH_TOKEN);
        }
    }

    private void validateRefreshToken(String userId, String refreshToken) {
        boolean isValidExpiry = jwtService.isValidTokenExpiry(refreshToken);
        String parsedUserId = jwtService.getSubject(refreshToken);
        boolean existsSavedRefreshToken = jwtService.existsSavedRefreshToken(userId, refreshToken);
        if (!isValidExpiry || !Objects.equals(parsedUserId, userId) || !existsSavedRefreshToken) {
            throw new BusinessException(INVALID_AUTH_TOKEN);
        }
    }
}
