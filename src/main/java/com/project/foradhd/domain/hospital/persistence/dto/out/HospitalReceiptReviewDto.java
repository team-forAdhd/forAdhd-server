package com.project.foradhd.domain.hospital.persistence.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReview;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class HospitalReceiptReviewDto {

    private HospitalReceiptReview hospitalReceiptReview;
    private UserProfile userProfile;
    private boolean isHelped;
    private boolean isMine;

    @QueryProjection
    public HospitalReceiptReviewDto(HospitalReceiptReview hospitalReceiptReview, UserProfile userProfile, boolean isHelped, boolean isMine) {
        this.hospitalReceiptReview = hospitalReceiptReview;
        this.userProfile = userProfile;
        this.isHelped = isHelped;
        this.isMine = isMine;
    }
}
