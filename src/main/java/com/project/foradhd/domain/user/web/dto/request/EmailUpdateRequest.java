package com.project.foradhd.domain.user.web.dto.request;

import com.project.foradhd.global.validation.annotation.ValidEmail;
import lombok.Getter;

@Getter
public class EmailUpdateRequest {

    @ValidEmail
    private String email;
}
