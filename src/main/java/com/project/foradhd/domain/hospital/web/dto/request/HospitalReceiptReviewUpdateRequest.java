package com.project.foradhd.domain.hospital.web.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class HospitalReceiptReviewUpdateRequest {

    private String content;
    private List<String> imageList;
    private Long medicalExpense;
}
