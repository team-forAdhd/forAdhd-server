package com.project.foradhd.domain.hospital.web.enums;

import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HospitalReviewType {

    RECEIPT_REVIEW(1), EVALUATION_REVIEW(2);

    private final int type;

    public static HospitalReviewType valueOf(int type) {
        for (HospitalReviewType hospitalReviewType : HospitalReviewType.values()) {
            if (hospitalReviewType.type == type) {
                return hospitalReviewType;
            }
        }
        throw new BusinessException(ErrorCode.INVALID_HOSPITAL_REVIEW_TYPE);
    }
}
