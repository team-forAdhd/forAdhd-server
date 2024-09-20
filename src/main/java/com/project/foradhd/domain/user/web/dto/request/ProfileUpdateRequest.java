package com.project.foradhd.domain.user.web.dto.request;

import com.project.foradhd.domain.user.persistence.enums.ForAdhdType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {

    @NotBlank(message = "{nickname.notBlank}")
    private String nickname;

    private String profileImage;

    @NotNull(message = "{forAdhdType.notNull}")
    private ForAdhdType forAdhdType;
}
