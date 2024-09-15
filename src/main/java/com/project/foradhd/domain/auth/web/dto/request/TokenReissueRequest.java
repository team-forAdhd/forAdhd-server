package com.project.foradhd.domain.auth.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenReissueRequest {

    @NotBlank(message = "{accessToken.notBlank}")
    private String accessToken;

    @NotBlank(message = "{refreshToken.notBlank}")
    private String refreshToken;
}
