package com.project.foradhd.domain.user.web.dto.response;

import com.project.foradhd.domain.user.persistence.enums.ForAdhdType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDetailsResponse {

    private String email;

    private String nickname;

    private String profileImage;

    private ForAdhdType forAdhdType;
}
