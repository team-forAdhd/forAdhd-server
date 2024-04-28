package com.project.foradhd.domain.user.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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

    @NotBlank(message = "{email.notBlank}")
    @Email(message = "{email.email}")
    private String email;

    private String password;

    private String passwordConfirm;

    @NotBlank(message = "{nickname.notBlank}")
    private String nickname;

    private String profileImage;

    @NotNull(message = "{isAdhd.notNull}")
    private Boolean isAdhd;

    @NotEmpty(message = "{termsApprovals.notEmpty}")
    private List<@Valid TermsApprovalRequest> termsApprovals;

    @NotEmpty(message = "{pushNotificationApprovals.notEmpty}")
    private List<@Valid PushNotificationApprovalRequest> pushNotificationApprovals;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermsApprovalRequest {

        @NotNull(message = "{termsId.notNull}")
        private Long termsId;

        @NotNull(message = "{terms.approved.notNull}")
        private Boolean approved;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PushNotificationApprovalRequest {

        @NotNull(message = "{pushNotificationApprovalId.notNull}")
        private Long pushNotificationApprovalId;

        @NotNull(message = "{pushNotificationApproval.approved.notNull}")
        private Boolean approved;
    }
}
