package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class SnsSignUpRequest {

    private String nickname;

    private String profileImage;

    private Boolean isAdhd;

    private List<TermsApprovalRequest> termsApprovals;

    private List<PushNotificationApprovalRequest> pushNotificationApprovals;

    @Getter
    public static class TermsApprovalRequest {

        private Long termsId;

        private Boolean approved;
    }

    @Getter
    public static class PushNotificationApprovalRequest {

        private Long pushNotificationApprovalId;

        private Boolean approved;
    }
}
