package com.project.foradhd.domain.auth.business.userdetails;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.enums.Role;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OAuth2Attributes 테스트")
class OAuth2AttributesTest {

    @DisplayName("처음 로그인한 소셜 유저라면 GUEST 권한 테스트")
    @Test
    void first_login_social_user_has_guest_role() {
        //given
        String email = "jkde7721@naver.com";
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
            .id("externalUserId")
            .name("김다은")
            .email(email)
            .isVerifiedEmail(true)
            .gender(Gender.FEMALE)
            .ageRange("20-30")
            .birth(LocalDate.of(1999, 2, 1))
            .provider(Provider.NAVER)
            .build();

        //when
        User user = oAuth2Attributes.toUserEntity();

        //then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getIsVerifiedEmail()).isTrue();
        assertThat(user.getRole()).isEqualTo(Role.GUEST);
    }
}
