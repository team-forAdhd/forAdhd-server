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
public class TermsApprovalsUpdateRequest {

    @NotEmpty(message = "{termsApprovals.notEmpty}")
    private List<@Valid TermsApprovalRequest> termsApprovals;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermsApprovalRequest {

        @NotNull(message = "{terms.id.notNull}")
        private Long termsId;

        @NotNull(message = "{terms.approved.notNull}")
        private Boolean approved;
    }
}
