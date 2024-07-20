package com.project.foradhd.domain.medicine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MedicineRequest {
    private String itemName;
    private String entpName;
    private String drugShape;
    private String colorClass1;
    private String chart;
    private String imageUrl;
}
