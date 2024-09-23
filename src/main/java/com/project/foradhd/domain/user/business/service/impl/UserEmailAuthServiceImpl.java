package com.project.foradhd.domain.user.business.service.impl;

import com.project.foradhd.domain.user.business.dto.in.EmailAuthData;
import com.project.foradhd.domain.user.business.dto.in.EmailAuthValidationData;
import com.project.foradhd.domain.user.business.service.UserEmailAuthService;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.service.CacheService;
import com.project.foradhd.global.service.EmailService;
import com.project.foradhd.global.util.EmailAuthCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.project.foradhd.global.enums.EmailTemplate.USER_EMAIL_AUTH_TEMPLATE;
import static com.project.foradhd.global.enums.CacheKeyType.USER_EMAIL_AUTH_CODE;
import static com.project.foradhd.global.exception.ErrorCode.EMAIL_AUTH_TIMEOUT;
import static java.util.concurrent.TimeUnit.MINUTES;

@RequiredArgsConstructor
@Service
public class UserEmailAuthServiceImpl implements UserEmailAuthService {

    private final UserService userService;
    private final EmailService emailService;
    private final CacheService cacheService;
    private static final int AUTH_CODE_TIMEOUT_MIN = 3;

    @Value("${cloud.aws.cloud-front.deploy-domain}")
    private String staticResourceHost;

    @Override
    public void authenticateEmail(String userId, EmailAuthData emailAuthData) {
        String email = emailAuthData.getEmail();
        if (isAnonymousUser(userId)) { //일반 회원가입 시 이메일 인증
            userService.validateDuplicatedEmail(email);
        } else { //소셜 회원가입 후 이메일 인증
            userService.validateDuplicatedEmail(email, userId);
        }

        String authCode = EmailAuthCodeGenerator.generate();
        cacheService.setValue(USER_EMAIL_AUTH_CODE, email, authCode, AUTH_CODE_TIMEOUT_MIN, MINUTES);
        emailService.sendEmail(USER_EMAIL_AUTH_TEMPLATE,
                Map.of("authCode", authCode, "staticResourceHost", staticResourceHost), email);
    }

    @Override
    public Optional<User> validateEmailAuth(String userId, EmailAuthValidationData emailAuthValidationData) {
        String email = emailAuthValidationData.getEmail();
        String authCode = emailAuthValidationData.getAuthCode();
        validateEmailAuthCode(email, authCode);

        if (isAnonymousUser(userId)) return Optional.empty();
        User user = userService.updateEmailAuth(userId, email);
        return Optional.of(user);
    }

    private boolean isAnonymousUser(String userId) {
        return "anonymousUser".equals(userId);
    }

    private void validateEmailAuthCode(String email, String authCode) {
        cacheService.getValue(USER_EMAIL_AUTH_CODE, email)
                .filter(savedAuthCode -> savedAuthCode.equals(authCode))
                .orElseThrow(() -> new BusinessException(EMAIL_AUTH_TIMEOUT));
        cacheService.deleteValue(USER_EMAIL_AUTH_CODE, email);
    }
}
