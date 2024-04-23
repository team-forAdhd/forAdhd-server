package com.project.foradhd.domain.user.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO: validation 체크
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private String name;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate birth;

    private Gender gender;

    @Email
    private String email;

    private String password;

    private String passwordConfirm;

    private String nickname;

    private String profileImage;

    private Boolean isAdhd;

    private List<TermsApprovalRequest> termsApprovals;

    private List<PushNotificationApprovalRequest> pushNotificationApprovals;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermsApprovalRequest {

        private Long termsId;

        private Boolean approved;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PushNotificationApprovalRequest {

        private Long pushNotificationApprovalId;

        private Boolean approved;
    }
}
