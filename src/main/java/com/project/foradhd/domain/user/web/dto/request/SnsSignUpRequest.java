package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class SnsSignUpRequest {

    private String nickname;

    private String profileImage;

    private Boolean isAdhd;

    private Boolean pushNotificationAgree;

    private List<SignUpRequest.TermsApprovalRequest> termsApprovals;

    @Getter
    public static class TermsApprovalRequest {

        private Long termsId;

        private Boolean approved;
    }
}
