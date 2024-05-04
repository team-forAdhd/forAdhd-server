package com.project.foradhd.domain.user.business.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailAuthValidationData {

    private String email;

    private String authCode;
}
