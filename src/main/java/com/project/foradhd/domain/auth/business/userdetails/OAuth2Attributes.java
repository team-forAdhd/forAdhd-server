package com.project.foradhd.domain.auth.business.userdetails;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.enums.Role;
import com.project.foradhd.global.util.NicknameGenerator;
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

    public User toEntity() {
        return User.builder()
            .snsUserId(id)
            .name(name)
            .nickname(NicknameGenerator.generate())
            .email(email)
            .isVerifiedEmail(isVerifiedEmail)
            .role(Role.GUEST)
            .gender(gender)
            .ageRange(ageRange)
            .birth(birth)
            .provider(provider)
            .build();
    }
}
