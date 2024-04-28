package com.project.foradhd.domain.user.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProfileUpdateRequest {

    @NotBlank(message = "{nickname.notBlank}")
    private String nickname;

    private String profileImage;

    @NotNull(message = "{isAdhd.notNull}")
    private Boolean isAdhd;
}
