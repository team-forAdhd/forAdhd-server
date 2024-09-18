package com.project.foradhd.domain.medicine.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MedicineFilteringResponse {
    private String itemSeq;
    private String itemName;
    private String entpName;
    private String chart;
    private String drugShape;
    private String colorClass1;
    private String formCodeName;
}
