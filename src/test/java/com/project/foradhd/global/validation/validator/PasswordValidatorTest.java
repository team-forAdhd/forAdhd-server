package com.project.foradhd.global.validation.validator;

import com.project.foradhd.domain.user.web.dto.request.PasswordRequest;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.project.foradhd.global.validation.validator.PasswordValidatorTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("PasswordValidator 테스트")
@ContextConfiguration(classes = PasswordValidatorTestConfig.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class PasswordValidatorTest {

    @Autowired
    PasswordValidator passwordValidator;

    @Mock
    ConstraintValidatorContext context;

    @DisplayName("비밀번호 유효성 검사 테스트")
    @Test
    void is_valid_password() {
        //given
        String password = "abcd1234!";
        String passwordConfirm = "abcd1234!";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isTrue();
    }

    @DisplayName("비밀번호 유효성 검사 테스트 - 실패: 비밀번호 공백")
    @Test
    void is_valid_password_fail_blank() {
        //given
        String password = "";
        String passwordConfirm = "";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isFalse();
    }

    @DisplayName("비밀번호 유효성 검사 테스트 - 실패: 숫자 없음")
    @Test
    void is_valid_password_fail_without_number() {
        //given
        String password = "abcdefgh!";
        String passwordConfirm = "abcdefgh!";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        given(context.buildConstraintViolationWithTemplate(anyString())).willReturn(builder);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isFalse();
    }

    @DisplayName("비밀번호 유효성 검사 테스트 - 실패: 영문자 없음")
    @Test
    void is_valid_password_fail_without_english_char() {
        //given
        String password = "12345678!";
        String passwordConfirm = "12345678!";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        given(context.buildConstraintViolationWithTemplate(anyString())).willReturn(builder);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isFalse();
    }

    @DisplayName("비밀번호 유효성 검사 테스트 - 실패: 특수문자 없음")
    @Test
    void is_valid_password_fail_without_special_char() {
        //given
        String password = "abcd1234";
        String passwordConfirm = "abcd1234";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        given(context.buildConstraintViolationWithTemplate(anyString())).willReturn(builder);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isFalse();
    }

    @DisplayName("비밀번호 유효성 검사 테스트 - 실패: 짧은 길이(8자 이상)")
    @Test
    void is_valid_password_fail_with_short_length() {
        //given
        String password = "abc123!";
        String passwordConfirm = "abc123!";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        given(context.buildConstraintViolationWithTemplate(anyString())).willReturn(builder);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isFalse();
    }

    @DisplayName("비밀번호 유효성 검사 테스트 - 실패: 공백 문자 포함")
    @Test
    void is_valid_password_fail_with_white_space_char() {
        //given
        String password = "abcd 1234!";
        String passwordConfirm = "abcd 1234!";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        given(context.buildConstraintViolationWithTemplate(anyString())).willReturn(builder);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isFalse();
    }

    @DisplayName("비밀번호 유효성 검사 테스트 - 실패: 비밀번호 확인 불일치")
    @Test
    void is_valid_password_fail_not_matches_password_confirm() {
        //given
        String password = "abcd1234!";
        String passwordConfirm = "!abcd1234!";
        PasswordRequest passwordRequest = new PasswordRequest(password, passwordConfirm);
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        given(context.buildConstraintViolationWithTemplate(anyString())).willReturn(builder);

        //when
        boolean isValidPassword = passwordValidator.isValid(passwordRequest, context);

        //then
        assertThat(isValidPassword).isFalse();
    }

    @TestConfiguration
    static class PasswordValidatorTestConfig {

        @Bean
        public MessageSource validationMessageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:/messages/validation");
            messageSource.setDefaultEncoding("UTF-8");
            return messageSource;
        }

        @Bean
        public PasswordValidator passwordValidator() {
            return new PasswordValidator(validationMessageSource());
        }
    }
}
