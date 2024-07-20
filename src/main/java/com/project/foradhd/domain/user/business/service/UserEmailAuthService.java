package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.EmailAuthData;
import com.project.foradhd.domain.user.business.dto.in.EmailAuthValidationData;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.service.AwsSesService;
import com.project.foradhd.global.service.RedisService;
import com.project.foradhd.global.util.EmailAuthCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.project.foradhd.global.enums.EmailTemplate.USER_EMAIL_AUTH_TEMPLATE;
import static com.project.foradhd.global.enums.RedisKeyType.USER_EMAIL_AUTH_CODE;
import static com.project.foradhd.global.exception.ErrorCode.EMAIL_AUTH_TIMEOUT;
import static java.util.concurrent.TimeUnit.MINUTES;

@RequiredArgsConstructor
@Service
public class UserEmailAuthService {

    private final UserService userService;
    private final AwsSesService awsSesService;
    private final RedisService redisService;
    private static final int AUTH_CODE_TIMEOUT_MIN = 3;

    @Value("${cloud.aws.cloud-front.deploy-domain}")
    private String staticResourceHost;

    public void authenticateEmail(String userId, EmailAuthData emailAuthData) {
        String email = emailAuthData.getEmail();
        userService.validateDuplicatedEmail(email, userId);

        String authCode = EmailAuthCodeGenerator.generate();
        redisService.setValue(USER_EMAIL_AUTH_CODE, email, authCode, AUTH_CODE_TIMEOUT_MIN, MINUTES);
        awsSesService.sendEmail(USER_EMAIL_AUTH_TEMPLATE,
                Map.of("authCode", authCode, "staticResourceHost", staticResourceHost), email);
    }

    public User validateEmailAuth(String userId, EmailAuthValidationData emailAuthValidationData) {
        String email = emailAuthValidationData.getEmail();
        String authCode = emailAuthValidationData.getAuthCode();
        validateEmailAuthCode(email, authCode);
        return userService.updateEmailAuth(userId, email);
    }

    private void validateEmailAuthCode(String email, String authCode) {
        redisService.getValue(USER_EMAIL_AUTH_CODE, email)
                .filter(savedAuthCode -> savedAuthCode.equals(authCode))
                .orElseThrow(() -> new BusinessException(EMAIL_AUTH_TIMEOUT));
        redisService.deleteValue(USER_EMAIL_AUTH_CODE, email);
    }
}
