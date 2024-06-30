package com.project.foradhd.domain.hospital.persistence.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class HospitalNearbyDto {

    private Hospital hospital;
    private double distance;
    private boolean isBookmarked;

    @QueryProjection
    public HospitalNearbyDto(Hospital hospital, double distance, boolean isBookmarked) {
        this.hospital = hospital;
        this.distance = distance;
        this.isBookmarked = isBookmarked;
    }
}
