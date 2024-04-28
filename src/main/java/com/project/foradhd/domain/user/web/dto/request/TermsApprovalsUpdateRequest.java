package com.project.foradhd.domain.user.web.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TermsApprovalsUpdateRequest {

    @NotEmpty(message = "{termsApprovals.notEmpty}")
    private List<@Valid TermsApprovalRequest> termsApprovals;

    @Getter
    public static class TermsApprovalRequest {

        @NotNull(message = "{termsId.notNull}")
        private Long termsId;

        @NotNull(message = "{terms.approved.notNull}")
        private Boolean approved;
    }
}
