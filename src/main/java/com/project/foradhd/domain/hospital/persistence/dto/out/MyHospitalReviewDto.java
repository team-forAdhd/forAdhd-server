package com.project.foradhd.domain.hospital.persistence.dto.out;

import lombok.*;

import java.time.LocalDateTime;

@Getter
public class MyHospitalReviewDto {

    private String hospitalReviewId;
    private String hospitalId;
    private String hospitalName;
    private int reviewType;
    private LocalDateTime createdAt;
    private String content;
    private String imageList;
}
