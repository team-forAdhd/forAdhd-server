package com.project.foradhd.domain.hospital.persistence.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReview;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class HospitalReceiptReviewDto {

    private HospitalReceiptReview hospitalReceiptReview;
    private UserProfile userProfile;
    private Doctor doctor;
    private boolean isHelped;
    private boolean isMine;

    @QueryProjection
    public HospitalReceiptReviewDto(HospitalReceiptReview hospitalReceiptReview, UserProfile userProfile, Doctor doctor, boolean isHelped, boolean isMine) {
        this.hospitalReceiptReview = hospitalReceiptReview;
        this.userProfile = userProfile;
        this.doctor = doctor;
        this.isHelped = isHelped;
        this.isMine = isMine;
    }
}
