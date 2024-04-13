package com.project.foradhd.domain.user.web.dto.request;

import lombok.Getter;

@Getter
public class ProfileUpdateRequest {

    private String nickname;

    private String profileImage;

    private Boolean isAdhd;
}
