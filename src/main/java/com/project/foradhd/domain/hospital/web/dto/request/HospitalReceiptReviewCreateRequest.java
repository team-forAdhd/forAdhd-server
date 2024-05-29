package com.project.foradhd.domain.hospital.web.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class HospitalReceiptReviewCreateRequest {

    private Integer kindness;
    private Integer adhdUnderstanding;
    private Integer enoughMedicalTime;
    private String content;
    private List<String> imageList;
}
