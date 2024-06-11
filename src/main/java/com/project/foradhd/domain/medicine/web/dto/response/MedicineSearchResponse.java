package com.project.foradhd.domain.medicine.web.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MedicineSearchResponse {
    private String itemImage; // 약 이미지 URL
    private String itemName;  // 약 이름
    private String itemEngName; // 약 영문명
    private String entpName;    // 제조사 이름
}
