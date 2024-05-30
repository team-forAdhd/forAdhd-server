package com.project.foradhd.domain.medicine.web.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class MedicineReviewResponse {
    private Long id;
    private String userId;
    private Long medicineId;
    private String content;
    private String images;
    private float grade;
    private int helpCount;
}
