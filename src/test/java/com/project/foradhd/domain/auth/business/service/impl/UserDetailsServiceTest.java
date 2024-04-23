package com.project.foradhd.domain.auth.business.service.impl;

import static com.project.foradhd.domain.auth.fixtures.AuthFixtures.toAuthPassword;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.project.foradhd.domain.auth.business.userdetails.impl.UserDetailsImpl;
import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import com.project.foradhd.domain.auth.persistence.repository.AuthPasswordRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@DisplayName("UserDetailsService 테스트")
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthPasswordRepository authPasswordRepository;

    @DisplayName("일반 로그인 유저 조회 테스트")
    @Test
    void load_user_by_username_test() {
        //given
        String email = "jkde7721@naver.com";
        User user = toUser()
            .email(email)
            .build();
        AuthPassword authPassword = toAuthPassword().build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(authPasswordRepository.findByUserId(user.getId())).willReturn(Optional.of(authPassword));

        //when
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

        //then
        then(userRepository).should(times(1)).findByEmail(email);
        then(authPasswordRepository).should(times(1)).findByUserId(user.getId());
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(authPassword.getPassword());
        assertThat(userDetails.getAuthorities()).containsExactly(new SimpleGrantedAuthority(user.getAuthority()));
        assertThat(userDetails.getUserId()).isEqualTo(user.getId());
    }

    @DisplayName("일반 로그인 유저 조회 테스트 - 실패: 해당 이메일 가진 유저 없음")
    @Test
    void load_user_by_username_test_fail_no_email_user() {
        //given
        String email = "jkde7721@naver.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
        then(userRepository).should(times(1)).findByEmail(email);
        then(authPasswordRepository).should(never()).findByUserId(anyString());
    }

    @DisplayName("일반 로그인 유저 조회 테스트 - 실패: 유저 비밀번호 없음")
    @Test
    void load_user_by_username_test_fail_no_user_password() {
        //given
        String email = "jkde7721@naver.com";
        User user = toUser()
            .email(email)
            .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(authPasswordRepository.findByUserId(user.getId())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
        then(userRepository).should(times(1)).findByEmail(email);
        then(authPasswordRepository).should(times(1)).findByUserId(user.getId());
    }
}
