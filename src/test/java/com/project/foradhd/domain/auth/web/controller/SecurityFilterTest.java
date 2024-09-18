package com.project.foradhd.domain.auth.web.controller;

import com.project.foradhd.config.SecurityTestConfig;
import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.user.business.service.UserEmailAuthService;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.business.service.UserTokenService;
import com.project.foradhd.domain.user.web.controller.UserController;
import com.project.foradhd.domain.user.web.mapper.UserMapper;
import com.project.foradhd.tags.IntegrationTag;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = SecurityTestConfig.class)
@WebMvcTest(value = { UserController.class, UserMapper.class})
class SecurityFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @MockBean
    UserTokenService userTokenService;

    @MockBean
    UserEmailAuthService userEmailAuthService;

    @Captor
    ArgumentCaptor<String> userIdArgumentCaptor;

    @Value("${jwt.expiry.access-token}")
    Long accessTokenExpiry;

    @DisplayName("인증 필터 태스트 - 성공: 유효한 토큰 및 권한")
    @Test
    void authentication_filter_test() throws Exception {
        //given
        String userId = "validUserId";
        String email = "jkde7721@naver.com";
        String accessToken = jwtService.generateAccessToken(userId, email, AuthorityUtils.createAuthorityList("ROLE_USER"));

        //when, then
        mockMvc.perform(get("/api/v1/user")
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());
        then(userService).should(times(1)).getUserProfileDetails(userIdArgumentCaptor.capture());
        assertThat(userIdArgumentCaptor.getValue()).isEqualTo(userId);
    }

    @DisplayName("인증 필터 테스트 - 실패: 유효하지 않은 토큰")
    @Test
    void authentication_filter_test_fail_invalid_token() throws Exception {
        //given
        String accessToken = "invalidToken";

        //when, then
        mockMvc.perform(get("/api/v1/user")
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @IntegrationTag
    @DisplayName("인증 필터 테스트 - 실패: 만료된 토큰")
    @Test
    void authentication_filter_test_fail_expired_token() throws Exception {
        //given
        String userId = "validUserId";
        String email = "jkde7721@naver.com";
        String accessToken = jwtService.generateAccessToken(userId, email, AuthorityUtils.createAuthorityList("ROLE_USER"));

        //지연 테스트 위한 Awaitility api 사용 (최대 10초 동안 1s 마다 조건 만족 여부 체크)
        await().atMost(accessTokenExpiry, TimeUnit.MILLISECONDS)
                .with()
                .pollDelay(Duration.ofMillis(1000))
                .untilAsserted(() -> assertThatThrownBy(() -> jwtService.validateTokenExpiry(accessToken))
                        .isInstanceOf(JwtException.class));

        //when, then
        mockMvc.perform(get("/api/v1/user")
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("인가 필터 테스트 - 실패: 권한 없음")
    @Test
    void authorization_filter_test_fail_forbidden() throws Exception {
        //given
        String userId = "validUserId";
        String email = "jkde7721@naver.com";
        String accessToken = jwtService.generateAccessToken(userId, email, AuthorityUtils.createAuthorityList("ROLE_GUEST"));

        //when, then
        mockMvc.perform(get("/api/v1/user")
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}
