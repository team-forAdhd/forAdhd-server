package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;

import com.project.foradhd.domain.user.persistence.enums.ForAdhdType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SnsSignUpRequest {

    @NotBlank(message = "{nickname.notBlank}")
    private String nickname;

    private String profileImage;

    @NotNull(message = "{forAdhdType.notNull}")
    private ForAdhdType forAdhdType;

    @NotEmpty(message = "{termsApprovals.notEmpty}")
    private List<@Valid TermsApprovalRequest> termsApprovals;

    @NotEmpty(message = "{pushNotificationApprovals.notEmpty}")
    private List<@Valid PushNotificationApprovalRequest> pushNotificationApprovals;

    @Getter
    public static class TermsApprovalRequest {

        @NotNull(message = "{terms.id.notNull}")
        private Long termsId;

        @NotNull(message = "{terms.approved.notNull}")
        private Boolean approved;
    }

    @Getter
    public static class PushNotificationApprovalRequest {

        @NotNull(message = "{pushNotificationApproval.id.notNull}")
        private Long pushNotificationApprovalId;

        @NotNull(message = "{pushNotificationApproval.approved.notNull}")
        private Boolean approved;
    }
}
