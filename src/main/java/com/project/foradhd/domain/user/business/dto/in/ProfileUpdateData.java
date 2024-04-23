package com.project.foradhd.domain.user.business.dto.in;

import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateData {

    private UserProfile userProfile;
}
