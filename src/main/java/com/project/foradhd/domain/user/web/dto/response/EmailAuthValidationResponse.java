package com.project.foradhd.domain.user.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailAuthValidationResponse {

    private String accessToken;
    private String refreshToken;
}
