package com.project.foradhd.domain.auth.business.service;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;

    public AuthTokenData reissue(String userId, String accessToken, String refreshToken) {
        validateAuthToken(userId, accessToken, refreshToken);

        User user = userService.getUser(userId);
        String reissuedAccessToken = jwtService.generateAccessToken(userId, user.getEmail(),
            createAuthorityList(user.getAuthority()));
        String reissuedRefreshToken = jwtService.generateRefreshToken(userId);
        //TODO: RT 저장 로직 구현
        return new AuthTokenData(reissuedAccessToken, reissuedRefreshToken);
    }

    private void validateAuthToken(String userId, String accessToken, String refreshToken) {
        if (!isValidAccessToken(userId, accessToken) ||
            !isValidRefreshToken(userId, refreshToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

    private boolean isValidAccessToken(String userId, String accessToken) {
        String parsedUserId = jwtService.getSubject(accessToken);
        return jwtService.isValidTokenForm(accessToken) && userId.equals(parsedUserId);
    }

    private boolean isValidRefreshToken(String userId, String refreshToken) {
        String parsedUserId = jwtService.getSubject(refreshToken);
        //TODO: RT 저장 여부 확인
        return jwtService.isValidTokenExpiry(refreshToken) && userId.equals(parsedUserId);
    }
}
