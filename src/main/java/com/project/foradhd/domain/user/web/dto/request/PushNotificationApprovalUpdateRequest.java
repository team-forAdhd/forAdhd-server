package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class PushNotificationApprovalUpdateRequest {

    private List<PushNotificationApprovalRequest> pushNotificationApprovals;

    @Getter
    public static class PushNotificationApprovalRequest {

        private Long pushNotificationApprovalId;

        private Boolean approved;
    }
}
