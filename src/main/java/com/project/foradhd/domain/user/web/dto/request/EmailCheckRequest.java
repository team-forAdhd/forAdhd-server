package com.project.foradhd.domain.user.web.dto.request;

import com.project.foradhd.global.validation.annotation.ValidEmail;
import lombok.Getter;

@Getter
public class EmailCheckRequest {

    @ValidEmail
    private String email;
}
