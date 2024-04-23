package com.project.foradhd.domain.user.business.dto.in;

import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushNotificationApprovalUpdateData {

    private List<UserPushNotificationApproval> userPushNotificationApprovals;
}
