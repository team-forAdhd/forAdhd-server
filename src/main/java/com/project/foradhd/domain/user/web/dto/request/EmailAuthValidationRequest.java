package com.project.foradhd.domain.user.web.dto.request;

import com.project.foradhd.global.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class EmailAuthValidationRequest {

    @ValidEmail
    private String email;

    @NotBlank(message = "{email.authCode.notBlank}")
    @Length(min = 6, max = 6, message = "{email.authCode.length}")
    private String authCode;
}
