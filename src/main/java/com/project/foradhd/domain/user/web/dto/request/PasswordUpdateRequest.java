package com.project.foradhd.domain.user.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordUpdateRequest {

    @NotBlank(message = "{prevPassword.notBlank}")
    private String prevPassword;

    @Valid
    private PasswordRequest password;
}
