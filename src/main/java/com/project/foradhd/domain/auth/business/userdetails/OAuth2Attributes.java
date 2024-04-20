package com.project.foradhd.domain.auth.business.userdetails;

import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.enums.Role;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OAuth2Attributes {

    protected String id;
    protected String name;
    protected String email;
    protected Boolean isVerifiedEmail;
    protected Gender gender;
    protected String ageRange;
    protected LocalDate birth;
    protected Provider provider;

    public User toUserEntity() {
        return User.builder()
            .email(email)
            .role(Role.GUEST)
            .isVerifiedEmail(isVerifiedEmail)
            .build();
    }

    public UserPrivacy toUserPrivacyEntity(User user) {
        return UserPrivacy.builder()
            .user(user)
            .name(name)
            .birth(birth)
            .ageRange(ageRange)
            .gender(gender)
            .build();
    }

    public AuthSocialLogin toAuthSocialLoginEntity(User user) {
        return AuthSocialLogin.builder()
            .user(user)
            .externalUserId(id)
            .provider(provider)
            .build();
    }
}
