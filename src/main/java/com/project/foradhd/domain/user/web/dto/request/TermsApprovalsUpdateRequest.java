package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class TermsApprovalsUpdateRequest {

    private List<TermsApprovalRequest> termsApprovals;

    @Getter
    public static class TermsApprovalRequest {

        private Long termsId;

        private Boolean approved;
    }
}
