package com.project.foradhd.domain.user.web.dto.request;

import com.project.foradhd.global.validation.annotation.ValidEmail;
import lombok.Getter;

@Getter
public class EmailAuthRequest {

    @ValidEmail
    private String email;
}
