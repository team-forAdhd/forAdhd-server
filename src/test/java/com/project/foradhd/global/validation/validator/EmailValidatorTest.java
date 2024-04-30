package com.project.foradhd.global.validation.validator;

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

import static com.project.foradhd.global.validation.validator.EmailValidatorTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("EmailValidator 테스트")
@ContextConfiguration(classes = EmailValidatorTestConfig.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class EmailValidatorTest {

    @Autowired
    EmailValidator emailValidator;

    @Mock
    ConstraintValidatorContext context;

    @DisplayName("이메일 형식 및 유효 도메인 테스트")
    @Test
    void is_valid_email() {
        //given
        String email = "jkde7721@naver.com";

        //when
        boolean isValidEmail = emailValidator.isValid(email, context);

        //then
        assertThat(isValidEmail).isTrue();
    }

    @DisplayName("이메일 형식 및 유효 도메인 테스트 - 실패: 유효하지 않은 형식")
    @Test
    void is_valid_email_fail_invalid_format() {
        //given
        String email = "jkde7721naver.com";

        //when
        boolean isValidEmail = emailValidator.isValid(email, context);

        //then
        assertThat(isValidEmail).isFalse();
    }

    @DisplayName("이메일 형식 및 유효 도메인 테스트 - 실패: 지원하지 않는 도메인")
    @Test
    void is_valid_email_fail_not_supported_domain() {
        //given
        String email = "jkde7721@fora.com";
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        given(context.buildConstraintViolationWithTemplate(anyString())).willReturn(builder);

        //when
        boolean isValidEmail = emailValidator.isValid(email, context);

        //then
        assertThat(isValidEmail).isFalse();
    }

    @TestConfiguration
    static class EmailValidatorTestConfig {

        @Bean
        public MessageSource validationMessageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:/messages/validation");
            messageSource.setDefaultEncoding("UTF-8");
            return messageSource;
        }

        @Bean
        public EmailValidator emailValidator() {
            return new EmailValidator(validationMessageSource());
        }
    }
}
