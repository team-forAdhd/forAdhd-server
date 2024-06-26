package com.project.foradhd.domain.auth.business.service.impl;

import static com.project.foradhd.domain.auth.fixtures.AuthFixtures.toAuthSocialLogin;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.project.foradhd.domain.auth.business.service.OAuth2UserInfoService;
import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;
import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin;
import com.project.foradhd.domain.auth.persistence.repository.AuthSocialLoginRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.enums.Role;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OAuth2UserService 테스트")
@ExtendWith(MockitoExtension.class)
class OAuth2UserServiceTest {

    @InjectMocks
    OAuth2UserServiceImpl oAuth2UserService;

    @Mock
    UserService userService;

    @Mock
    OAuth2UserInfoService oAuth2UserInfoService;

    @Mock
    AuthSocialLoginRepository authSocialLoginRepository;

    @DisplayName("기존 일반 유저 로그인 테스트: USER Role 획득 + 소셜 계정 자동 연동")
    @Test
    void login_user_test() {
        //given
        String email = "jkde7721@naver.com";
        User user = toUser()
            .email(email)
            .isVerifiedEmail(false)
            .role(Role.GUEST)
            .build();
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
            .id("externalUserId")
            .name("김다은")
            .email(email)
            .isVerifiedEmail(true)
            .gender(Gender.FEMALE)
            .ageRange("20~30")
            .birth(LocalDate.of(1999, 2, 1))
            .provider(Provider.NAVER)
            .build();
        given(userService.hasUserProfile(user.getId())).willReturn(true);
        given(authSocialLoginRepository.findByProviderAndExternalUserId(oAuth2Attributes.getProvider(), oAuth2Attributes.getId()))
            .willReturn(Optional.empty());

        //when
        User socialLoginedUser = oAuth2UserService.loginSocialUser(user, oAuth2Attributes);

        //then
        assertThat(socialLoginedUser.getRole()).isEqualTo(Role.USER);
        then(userService).should(times(1)).hasUserProfile(user.getId());
        then(authSocialLoginRepository).should(times(1))
            .findByProviderAndExternalUserId(oAuth2Attributes.getProvider(), oAuth2Attributes.getId());
        then(authSocialLoginRepository).should(times(1)).save(any(AuthSocialLogin.class));
    }

    @DisplayName("기존 소셜 유저 로그인 테스트: 추가 회원가입 미진행으로 GUEST Role")
    @Test
    void login_social_user_test() {
        //given
        String email = "jkde7721@naver.com";
        User user = toUser()
            .email(email)
            .isVerifiedEmail(true)
            .role(Role.GUEST)
            .build();
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
            .id("externalUserId")
            .name("김다은")
            .email(email)
            .isVerifiedEmail(true)
            .gender(Gender.FEMALE)
            .ageRange("20~30")
            .birth(LocalDate.of(1999, 2, 1))
            .provider(Provider.NAVER)
            .build();
        AuthSocialLogin authSocialLogin = toAuthSocialLogin()
            .user(user)
            .build();
        given(userService.hasUserProfile(user.getId())).willReturn(false);
        given(authSocialLoginRepository.findByProviderAndExternalUserId(oAuth2Attributes.getProvider(), oAuth2Attributes.getId()))
            .willReturn(Optional.of(authSocialLogin));

        //when
        User socialLoginedUser = oAuth2UserService.loginSocialUser(user, oAuth2Attributes);

        //then
        assertThat(socialLoginedUser.getRole()).isEqualTo(Role.GUEST);
        then(userService).should(times(1)).hasUserProfile(user.getId());
        then(authSocialLoginRepository).should(times(1))
            .findByProviderAndExternalUserId(oAuth2Attributes.getProvider(), oAuth2Attributes.getId());
        then(authSocialLoginRepository).should(never()).save(any(AuthSocialLogin.class));
    }

    @DisplayName("새로운 소셜 유저 회원가입 테스트")
    @Test
    void sign_up_social_user_test() {
        //given
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
            .id("externalUserId")
            .name("김다은")
            .email("jkde7721@naver.com")
            .isVerifiedEmail(true)
            .gender(Gender.FEMALE)
            .ageRange("20~30")
            .birth(LocalDate.of(1999, 2, 1))
            .provider(Provider.NAVER)
            .build();

        //when
        oAuth2UserService.signUpSocialUser(oAuth2Attributes);

        //then
        then(userService).should(times(1)).saveUserInfo(any(User.class), any(UserPrivacy.class));
        then(authSocialLoginRepository).should(times(1)).save(any(AuthSocialLogin.class));
    }
}
