package com.project.foradhd.domain.user.web.dto.request;

import lombok.Getter;

@Getter
public class PasswordUpdateRequest {

    private String prevPassword;

    private String password;

    private String passwordConfirm;
}
