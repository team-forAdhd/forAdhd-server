package com.project.foradhd.domain.hospital.persistence.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class HospitalBookmarkDto {

    private Hospital hospital;
    private double distance;

    @QueryProjection
    public HospitalBookmarkDto(Hospital hospital, double distance) {
        this.hospital = hospital;
        this.distance = distance;
    }
}
