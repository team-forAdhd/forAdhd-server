package com.project.foradhd.domain.auth.business.service.impl;

import static com.project.foradhd.domain.auth.fixtures.AuthFixtures.toAuthPassword;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static com.project.foradhd.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.project.foradhd.global.exception.ErrorCode.NOT_MATCH_USERNAME_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.auth.persistence.repository.AuthPasswordRepository;
import com.project.foradhd.domain.user.business.service.UserAuthInfoService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.config.PasswordEncoderConfig;
import java.util.Optional;

import com.project.foradhd.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("UserAuthInfoService 테스트")
@ContextConfiguration(classes = PasswordEncoderConfig.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class UserAuthInfoServiceTest {

    UserAuthInfoService userAuthInfoService;

    @Mock
    AuthPasswordRepository authPasswordRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Captor
    ArgumentCaptor<AuthPassword> authPasswordArgumentCaptor;
    
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        userAuthInfoService = new UserAuthInfoServiceImpl(authPasswordRepository, passwordEncoder);
    }

    @DisplayName("일반 회원가입 시 비밀번호 저장 테스트")
    @Test
    void sign_up_by_password_test() {
        //given
        User user = toUser().build();
        String password = "password";

        //when
        userAuthInfoService.signUpByPassword(user, password);

        //then
        then(authPasswordRepository).should(times(1)).save(authPasswordArgumentCaptor.capture());
        AuthPassword authPassword = authPasswordArgumentCaptor.getValue();
        assertThat(authPassword.getUser()).isEqualTo(user);
        assertThat(passwordEncoder.matches(password, authPassword.getPassword())).isTrue();
    }

    @DisplayName("비밀번호 일치 여부 검증 테스트")
    @Test
    void validate_password_matches_test() {
        //given
        String userId = "userId";
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        AuthPassword authPassword = toAuthPassword()
            .password(encodedPassword)
            .build();
        given(authPasswordRepository.findByUserId(userId)).willReturn(Optional.of(authPassword));

        //when, then
        assertThatNoException()
            .isThrownBy(() -> userAuthInfoService.validatePasswordMatches(userId, password));
        then(authPasswordRepository).should(times(1)).findByUserId(userId);
    }

    @DisplayName("비밀번호 일치 여부 검증 테스트 - 실패: 비밀번호 불일치")
    @Test
    void validate_password_matches_test_fail_not_match() {
        //given
        String userId = "userId";
        String password = "password";
        String anotherPassword = "anotherPassword";
        String encodedAnotherPassword = passwordEncoder.encode(anotherPassword);
        AuthPassword authPassword = toAuthPassword()
            .password(encodedAnotherPassword)
            .build();
        given(authPasswordRepository.findByUserId(userId)).willReturn(Optional.of(authPassword));

        //when, then
        assertThatThrownBy(() -> userAuthInfoService.validatePasswordMatches(userId, password))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_MATCH_USERNAME_PASSWORD);
        then(authPasswordRepository).should(times(1)).findByUserId(userId);
    }

    @DisplayName("비밀번호 수정 테스트")
    @Test
    void update_password_test() {
        //given
        String userId = "userId";
        String password = "password";
        String newPassword = "newPassword";
        String encodedPassword = passwordEncoder.encode(password);
        AuthPassword authPassword = toAuthPassword()
            .password(encodedPassword)
            .build();
        given(authPasswordRepository.findByUserId(userId)).willReturn(Optional.of(authPassword));

        //when
        userAuthInfoService.updatePassword(userId, newPassword);

        //then
        then(authPasswordRepository).should(times(1)).findByUserId(userId);
        assertThat(passwordEncoder.matches(newPassword, authPassword.getPassword())).isTrue();
    }

    @DisplayName("유저 ID로 비밀번호 정보 조회 테스트 - 실패: 일반 회원가입 유저 아님")
    @Test
    void get_auth_password_test_fail_not_signed_up_user() {
        //given
        String userId = "userId";
        given(authPasswordRepository.findByUserId(userId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userAuthInfoService.getAuthPassword(userId))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_USER);
    }
}
