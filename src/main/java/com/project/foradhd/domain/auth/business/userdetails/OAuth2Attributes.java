package com.project.foradhd.domain.auth.business.userdetails;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public abstract class OAuth2Attributes {

    protected String name;
    protected String email;
    protected Gender gender;
    protected String ageRange;
    protected LocalDate birth;

    public User toEntity() {
        //TODO: 유저 role 추가
        return User.builder()
            .name(name)
            .email(email)
            .gender(gender)
//        .ageRange(ageRange)
            .birth(birth)
            .build();
    }
}
