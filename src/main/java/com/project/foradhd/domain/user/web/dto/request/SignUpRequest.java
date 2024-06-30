package com.project.foradhd.domain.user.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.project.foradhd.domain.user.persistence.enums.ForAdhdType;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.validation.annotation.ValidEmail;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "{name.notBlank}")
    private String name;

    @NotNull(message = "{birth.notNull}")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate birth;

    @NotNull(message = "{gender.notNull}")
    private Gender gender;

    @ValidEmail
    private String email;

    @Valid
    private PasswordRequest password;

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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermsApprovalRequest {

        @NotNull(message = "{terms.id.notNull}")
        private Long termsId;

        @NotNull(message = "{terms.approved.notNull}")
        private Boolean approved;
    }

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
