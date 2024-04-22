package com.project.foradhd.domain.auth.business.service;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;
import com.project.foradhd.domain.auth.business.service.impl.JwtServiceImpl;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("AuthService 테스트")
@TestPropertySource(properties = {"jwt.expiry.access-token=100", "jwt.expiry.refresh-token=3600000",
    "jwt.secret-key=ThisIsSimplyAJwtSecretKeyForTestingPurposesThereAreNoSecurityIssuesAtAll"})
@Import(JwtServiceImpl.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class AuthServiceTest {

    AuthService authService;

    @Autowired
    JwtService jwtService;

    @Mock
    UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.authService = new AuthService(jwtService, userService);
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
        given(userService.getUser(userId)).willReturn(user);

        //when
        Thread.sleep(1000); //토큰 생성 시 Seed Time 다르게 설정하기 위해
        AuthTokenData authTokenData = authService.reissue(accessToken, refreshToken);

        //then
        assertThat(authTokenData.getAccessToken()).isNotEqualTo(accessToken);
        assertThat(authTokenData.getRefreshToken()).isNotEqualTo(refreshToken);
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
            .isInstanceOf(RuntimeException.class)
            .hasMessage("유효하지 않은 토큰입니다.");
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
            .isInstanceOf(RuntimeException.class)
            .hasMessage("유효하지 않은 토큰입니다.");
        then(userService).should(never()).getUser(userId);
    }
}
