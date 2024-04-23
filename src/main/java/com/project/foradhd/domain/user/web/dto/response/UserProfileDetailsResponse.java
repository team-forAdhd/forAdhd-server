package com.project.foradhd.domain.user.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDetailsResponse {

    private String email;

    private String nickname;

    private String profileImage;

    private Boolean isAdhd;
}
