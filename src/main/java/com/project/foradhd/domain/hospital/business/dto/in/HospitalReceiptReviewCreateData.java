package com.project.foradhd.domain.hospital.business.dto.in;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalReceiptReviewCreateData {

    private String content;
    private List<String> imageList;
    private Long medicalExpense;
}
