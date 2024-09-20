package com.project.foradhd.domain.user.web.dto.request;

import com.project.foradhd.global.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthValidationRequest {

    @ValidEmail
    private String email;

    @NotBlank(message = "{email.authCode.notBlank}")
    @Length(min = 6, max = 6, message = "{email.authCode.length}")
    private String authCode;
}
