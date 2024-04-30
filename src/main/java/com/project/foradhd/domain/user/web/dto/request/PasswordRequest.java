package com.project.foradhd.domain.user.web.dto.request;

import com.project.foradhd.global.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidPassword
public class PasswordRequest {

    private String password;

    private String passwordConfirm;
}
