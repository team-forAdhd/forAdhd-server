package com.project.foradhd.domain.hospital.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalReceiptReviewUpdateRequest {

    private String content;
    private List<String> imageList;
    private Long medicalExpense;
}
