package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PushNotificationApprovalUpdateRequest {

    @NotEmpty(message = "{pushNotificationApprovals.notEmpty}")
    private List<@Valid PushNotificationApprovalRequest> pushNotificationApprovals;

    @Getter
    public static class PushNotificationApprovalRequest {

        @NotNull(message = "{pushNotificationApprovalId.notNull}")
        private Long pushNotificationApprovalId;

        @NotNull(message = "{pushNotificationApproval.approved.notNull}")
        private Boolean approved;
    }
}
