package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.EmailAuthValidationData;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.service.AwsSesService;
import com.project.foradhd.global.service.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.project.foradhd.global.exception.ErrorCode.EMAIL_AUTH_TIMEOUT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("UserEmailAuthService 테스트")
@ExtendWith(MockitoExtension.class)
class UserEmailAuthServiceTest {

    @InjectMocks
    UserEmailAuthService userEmailAuthService;

    @Mock
    UserService userService;

    @Mock
    AwsSesService awsSesService;

    @Mock
    RedisService redisService;

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
        given(redisService.getValue(email)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userEmailAuthService.validateEmailAuth(userId, emailAuthValidationData))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(EMAIL_AUTH_TIMEOUT);
        then(userService).should(never()).updateEmailAuth(anyString());
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
        given(redisService.getValue(email)).willReturn(Optional.of(savedAuthCode));

        //when, then
        assertThatThrownBy(() -> userEmailAuthService.validateEmailAuth(userId, emailAuthValidationData))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(EMAIL_AUTH_TIMEOUT);
        then(userService).should(never()).updateEmailAuth(anyString());
    }
}
