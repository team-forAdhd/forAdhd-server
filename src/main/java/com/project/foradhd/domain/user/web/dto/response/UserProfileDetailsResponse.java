package com.project.foradhd.domain.user.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDetailsResponse {

    private String name;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate birth;

    private Gender gender;

    private String email;

    private String nickname;

    private String profileImage;

    private Boolean isAdhd;

    private Boolean pushNotificationAgree;

    private List<TermsApprovalResponse> termsApprovals;

    @Getter
    @Builder
    public static class TermsApprovalResponse {

        private Long termsId;

        private Boolean approved;
    }
}
