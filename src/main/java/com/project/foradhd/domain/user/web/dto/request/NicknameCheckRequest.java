package com.project.foradhd.domain.user.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameCheckRequest {

    @NotBlank(message = "{nickname.notBlank}")
    private String nickname;
}
