package com.project.foradhd.domain.user.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserBlockRequest {

    private String blockedUserId;
    private Boolean isBlocked;
}
