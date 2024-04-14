package com.project.foradhd.domain.user.business.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordUpdateData {

    private String prevPassword;

    private String password;

    private String passwordConfirm;
}
