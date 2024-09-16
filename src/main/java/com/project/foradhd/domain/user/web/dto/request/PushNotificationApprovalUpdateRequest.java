package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationApprovalUpdateRequest {

    @NotEmpty(message = "{pushNotificationApprovals.notEmpty}")
    private List<@Valid PushNotificationApprovalRequest> pushNotificationApprovals;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PushNotificationApprovalRequest {

        @NotNull(message = "{pushNotificationApproval.id.notNull}")
        private Long pushNotificationApprovalId;

        @NotNull(message = "{pushNotificationApproval.approved.notNull}")
        private Boolean approved;
    }
}
