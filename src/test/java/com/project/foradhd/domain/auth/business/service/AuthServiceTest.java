package com.project.foradhd.domain.auth.business.service;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static com.project.foradhd.global.enums.CacheKeyType.USER_REFRESH_TOKEN;
import static com.project.foradhd.global.exception.ErrorCode.INVALID_AUTH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;
import com.project.foradhd.domain.auth.business.service.impl.AuthServiceImpl;
import com.project.foradhd.domain.auth.business.service.impl.JwtServiceImpl;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DisplayName("AuthService 테스트")
@TestPropertySource(properties = {"jwt.expiry.access-token=100", "jwt.expiry.refresh-token=3600000",
    "jwt.secret-key=ThisIsSimplyAJwtSecretKeyForTestingPurposesThereAreNoSecurityIssuesAtAll"})
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class AuthServiceTest {

    AuthService authService;

    JwtService jwtService;

    @Mock
    CacheService cacheService;

    @Value("${jwt.expiry.access-token}")
    Long accessTokenExpiry;

    @Value("${jwt.expiry.refresh-token}")
    Long refreshTokenExpiry;

    @Value("${jwt.secret-key}")
    String secretKey;

    @Mock
    UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.jwtService = new JwtServiceImpl(cacheService, accessTokenExpiry, refreshTokenExpiry, secretKey);
        this.authService = new AuthServiceImpl(jwtService, userService);
    }

    @DisplayName("AT 만료 시 토큰 재발급 테스트")
    @Test
    void reissue_test() throws InterruptedException {
        //given
        String userId = "userId";
        String email = "jkde7721@naver.com";
        String accessToken = jwtService.generateAccessToken(userId, email, createAuthorityList("ROLE_USER"));
        String refreshToken = jwtService.generateRefreshToken(userId);
        User user = toUser().build();
        given(cacheService.getValue(USER_REFRESH_TOKEN, userId)).willReturn(Optional.of(refreshToken));
        given(userService.getUser(userId)).willReturn(user);

        //when
        Thread.sleep(1000); //토큰 생성 시 Seed Time 다르게 설정하기 위해
        AuthTokenData authTokenData = authService.reissue(accessToken, refreshToken);

        //then
        assertThat(authTokenData.getAccessToken()).isNotEqualTo(accessToken);
        assertThat(authTokenData.getRefreshToken()).isNotEqualTo(refreshToken);
        then(cacheService).should(times(1)).getValue(USER_REFRESH_TOKEN, userId);
        then(userService).should(times(1)).getUser(userId);
    }

    @DisplayName("AT 만료 시 토큰 재발급 테스트 - 실패: 유효하지 않은 AT")
    @Test
    void reissue_test_fail_invalid_access_token() {
        //given
        String userId = "userId";
        String accessToken = "accessToken";
        String refreshToken = jwtService.generateRefreshToken(userId);

        //when, then
        assertThatThrownBy(() -> authService.reissue(accessToken, refreshToken))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_AUTH_TOKEN);
        then(userService).should(never()).getUser(userId);
    }

    @DisplayName("AT 만료 시 토큰 재발급 테스트 - 실패: 다른 유저의 RT")
    @Test
    void reissue_test_fail_another_user_refresh_token() {
        //given
        String userId = "userId";
        String anotherUserId = "anotherUserId";
        String email = "jkde7721@naver.com";
        String accessToken = jwtService.generateAccessToken(userId, email, createAuthorityList("ROLE_USER"));
        String refreshToken = jwtService.generateRefreshToken(anotherUserId);

        //when, then
        assertThatThrownBy(() -> authService.reissue(accessToken, refreshToken))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_AUTH_TOKEN);
        then(userService).should(never()).getUser(userId);
    }

    @DisplayName("AT 만료 시 토큰 재발급 테스트 - 실패: 저장된 토큰과 다름")
    @Test
    void reissue_test_fail_not_matches_with_saved_refresh_token() {
        //given
        String userId = "userId";
        String email = "jkde7721@naver.com";
        String accessToken = jwtService.generateAccessToken(userId, email, createAuthorityList("ROLE_USER"));
        String refreshToken = jwtService.generateRefreshToken(userId);
        String savedRefreshToken = "savedRefreshToken";
        given(cacheService.getValue(USER_REFRESH_TOKEN, userId)).willReturn(Optional.of(savedRefreshToken));

        //when, then
        assertThatThrownBy(() -> authService.reissue(accessToken, refreshToken))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_AUTH_TOKEN);
        then(cacheService).should(times(1)).getValue(USER_REFRESH_TOKEN, userId);
        then(userService).should(never()).getUser(userId);
    }
}
