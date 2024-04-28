package com.project.foradhd.domain.user.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailUpdateRequest {

    @NotBlank(message = "{email.notBlank}")
    @Email(message = "{email.email}")
    private String email;
}
