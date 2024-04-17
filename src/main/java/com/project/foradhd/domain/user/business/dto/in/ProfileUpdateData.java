package com.project.foradhd.domain.user.business.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateData {

    private String nickname;

    private String profileImage;

    private Boolean isAdhd;
}
