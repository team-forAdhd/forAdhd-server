package com.project.foradhd.domain.user.business.dto.in;

import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SnsSignUpData {

    private UserProfile userProfile;
    private List<UserTermsApproval> userTermsApprovals;
    private List<UserPushNotificationApproval> userPushNotificationApprovals;
}
