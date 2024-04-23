package com.project.foradhd.domain.user.business.dto.in;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpData {

    private User user;
    private UserPrivacy userPrivacy;
    private UserProfile userProfile;
    private String password;
    private List<UserTermsApproval> userTermsApprovals;
    private List<UserPushNotificationApproval> userPushNotificationApprovals;
}
