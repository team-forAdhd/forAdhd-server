package com.project.foradhd.domain.hospital.persistence.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class HospitalNearbyDto {

    private Hospital hospital;
    private long totalGradeSum;
    private int totalBriefReviewCount;
    private int totalReceiptReviewCount;
    private double distance;
    private boolean isBookmarked;

    @QueryProjection
    public HospitalNearbyDto(Hospital hospital, long totalGradeSum, int totalBriefReviewCount, int totalReceiptReviewCount, double distance, boolean isBookmarked) {
        this.hospital = hospital;
        this.totalGradeSum = totalGradeSum;
        this.totalBriefReviewCount = totalBriefReviewCount;
        this.totalReceiptReviewCount = totalReceiptReviewCount;
        this.distance = distance;
        this.isBookmarked = isBookmarked;
    }
}
