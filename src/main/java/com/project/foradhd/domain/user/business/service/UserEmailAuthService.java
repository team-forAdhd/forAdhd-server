package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.EmailAuthData;
import com.project.foradhd.domain.user.business.dto.in.EmailAuthValidationData;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.service.AwsSesService;
import com.project.foradhd.global.service.RedisService;
import com.project.foradhd.global.util.EmailAuthCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.project.foradhd.global.enums.EmailTemplate.USER_EMAIL_AUTH_TEMPLATE;
import static com.project.foradhd.global.enums.RedisKey.USER_EMAIL_AUTH_CODE_KEY;
import static com.project.foradhd.global.exception.ErrorCode.EMAIL_AUTH_TIMEOUT;
import static java.util.concurrent.TimeUnit.MINUTES;

@RequiredArgsConstructor
@Service
public class UserEmailAuthService {

    private final UserService userService;
    private final AwsSesService awsSesService;
    private final RedisService redisService;
    private static final int AUTH_CODE_TIMEOUT_MIN = 3;

    public void authenticateEmail(String userId, EmailAuthData emailAuthData) {
        String email = emailAuthData.getEmail();
        userService.validateDuplicatedEmail(email, userId);

        String authCode = EmailAuthCodeGenerator.generate();
        redisService.setValue(USER_EMAIL_AUTH_CODE_KEY.getKey(email), authCode, AUTH_CODE_TIMEOUT_MIN, MINUTES);
        awsSesService.sendEmail(USER_EMAIL_AUTH_TEMPLATE, Map.of("authCode", authCode), email);
    }

    public void validateEmailAuth(String userId, EmailAuthValidationData emailAuthValidationData) {
        String email = emailAuthValidationData.getEmail();
        String authCode = emailAuthValidationData.getAuthCode();
        validateEmailAuthCode(email, authCode);
        userService.updateEmailAuth(userId);
    }

    private void validateEmailAuthCode(String email, String authCode) {
        redisService.getValue(USER_EMAIL_AUTH_CODE_KEY.getKey(email))
                .filter(savedAuthCode -> savedAuthCode.equals(authCode))
                .orElseThrow(() -> new BusinessException(EMAIL_AUTH_TIMEOUT));
        redisService.deleteValue(USER_EMAIL_AUTH_CODE_KEY.getKey(email));
    }
}
