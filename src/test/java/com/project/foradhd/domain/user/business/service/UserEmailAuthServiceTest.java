package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.EmailAuthData;
import com.project.foradhd.domain.user.business.dto.in.EmailAuthValidationData;
import com.project.foradhd.domain.user.business.service.impl.UserEmailAuthServiceImpl;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.service.CacheService;
import com.project.foradhd.global.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static com.project.foradhd.global.enums.CacheKeyType.USER_EMAIL_AUTH_CODE;
import static com.project.foradhd.global.exception.ErrorCode.EMAIL_AUTH_TIMEOUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("UserEmailAuthService 테스트")
@TestPropertySource(properties = "cloud.aws.cloud-front.deploy-domain=test-domain")
@Import(UserEmailAuthServiceImpl.class)
@ExtendWith(SpringExtension.class)
class UserEmailAuthServiceTest {

    @Autowired
    UserEmailAuthServiceImpl userEmailAuthService;

    @MockBean
    UserService userService;

    @MockBean
    EmailService emailService;

    @MockBean
    CacheService cacheService;

    @DisplayName("유저 이메일 인증코드 발급 테스트 : 성공 - GUEST 권한 이상 사용자")
    @Test
    void authenticate_email_test_with_guest_user() {
        //given
        String userId = "userId";
        String email = "jkde7721@naver.com";
        EmailAuthData emailAuthData = EmailAuthData.builder().email(email).build();

        //when
        userEmailAuthService.authenticateEmail(userId, emailAuthData);

        //then
        then(userService).should(never()).validateDuplicatedEmail(email);
        then(userService).should(times(1)).validateDuplicatedEmail(email, userId);
    }

    @DisplayName("유저 이메일 인증코드 발급 테스트 : 성공 - 익명 사용자")
    @Test
    void authenticate_email_test_with_anonymous_user() {
        //given
        String userId = "anonymousUser";
        String email = "jkde7721@naver.com";
        EmailAuthData emailAuthData = EmailAuthData.builder().email(email).build();

        //when
        userEmailAuthService.authenticateEmail(userId, emailAuthData);

        //then
        then(userService).should(times(1)).validateDuplicatedEmail(email);
        then(userService).should(never()).validateDuplicatedEmail(email, userId);
    }

    @DisplayName("유저 이메일 인증코드 검증 테스트 : 성공 - GUEST 권한 이상 사용자")
    @Test
    void validate_email_auth_test_with_guest_user() {
        //given
        String userId = "userId";
        String email = "jkde7721@naver.com";
        String authCode = "123456";
        EmailAuthValidationData emailAuthValidationData = EmailAuthValidationData.builder()
                .email(email)
                .authCode(authCode)
                .build();
        User user = toUser().id(userId).build();
        given(cacheService.getValue(USER_EMAIL_AUTH_CODE, email)).willReturn(Optional.of(authCode));
        given(userService.updateEmailAuth(userId, email)).willReturn(user);

        //when
        Optional<User> optionalUser = userEmailAuthService.validateEmailAuth(userId, emailAuthValidationData);

        //then
        then(cacheService).should(times(1)).getValue(USER_EMAIL_AUTH_CODE, email);
        then(userService).should(times(1)).updateEmailAuth(userId, email);
        assertThat(optionalUser).contains(user);
    }

    @DisplayName("유저 이메일 인증코드 검증 테스트 : 성공 - 익명 사용자")
    @Test
    void validate_email_auth_test_with_anonymous_user() {
        //given
        String userId = "anonymousUser";
        String email = "jkde7721@naver.com";
        String authCode = "123456";
        EmailAuthValidationData emailAuthValidationData = EmailAuthValidationData.builder()
                .email(email)
                .authCode(authCode)
                .build();
        given(cacheService.getValue(USER_EMAIL_AUTH_CODE, email)).willReturn(Optional.of(authCode));

        //when
        Optional<User> optionalUser = userEmailAuthService.validateEmailAuth(userId, emailAuthValidationData);

        //then
        then(cacheService).should(times(1)).getValue(USER_EMAIL_AUTH_CODE, email);
        then(userService).should(never()).updateEmailAuth(userId, email);
        assertThat(optionalUser).isEmpty();
    }

    @DisplayName("유저 이메일 인증코드 검증 테스트 : 실패 - 인증 가능 시간 초과")
    @Test
    void validate_email_auth_test_fail_timeout() {
        //given
        String userId = "userId";
        String email = "jkde7721@naver.com";
        String authCode = "123456";
        EmailAuthValidationData emailAuthValidationData = EmailAuthValidationData.builder()
                .email(email)
                .authCode(authCode)
                .build();
        given(cacheService.getValue(USER_EMAIL_AUTH_CODE, email)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userEmailAuthService.validateEmailAuth(userId, emailAuthValidationData))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(EMAIL_AUTH_TIMEOUT);
        then(userService).should(never()).updateEmailAuth(anyString(), anyString());
    }

    @DisplayName("유저 이메일 인증코드 검증 테스트 : 실패 - 인증코드 불일치")
    @Test
    void validate_email_auth_test_fail_not_matches_auth_code() {
        //given
        String userId = "userId";
        String email = "jkde7721@naver.com";
        String authCode = "123456";
        String savedAuthCode = "654321";
        EmailAuthValidationData emailAuthValidationData = EmailAuthValidationData.builder()
                .email(email)
                .authCode(authCode)
                .build();
        given(cacheService.getValue(USER_EMAIL_AUTH_CODE, email)).willReturn(Optional.of(savedAuthCode));

        //when, then
        assertThatThrownBy(() -> userEmailAuthService.validateEmailAuth(userId, emailAuthValidationData))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(EMAIL_AUTH_TIMEOUT);
        then(userService).should(never()).updateEmailAuth(anyString(), anyString());
    }
}
