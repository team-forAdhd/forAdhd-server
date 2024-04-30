package com.project.foradhd.domain.user.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameCheckRequest {

    @NotBlank(message = "{nickname.notBlank}")
    private String nickname;
}
