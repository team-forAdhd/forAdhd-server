package com.project.foradhd.domain.user.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

//TODO: validation 체크
@ToString
@Getter
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

    private Boolean pushNotificationAgree;

    private List<TermsApproval> termsApprovals;

    @ToString
    @Getter
    public static class TermsApproval {

        private Long termsId;

        private Boolean approved;
    }
}
