package com.project.foradhd.domain.user.business.dto.out;

import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDetailsData {

    private UserProfile userProfile;
}
