package com.project.foradhd.domain.medicine.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineDto {
    private String itemSeq;
    private String itemName;
    private String entpName;
    private String chart;
    private String itemImage;

    public MedicineDto(String itemSeq, String itemName, String entpName, String chart, String itemImage) {
        this.itemSeq = itemSeq;
        this.itemName = itemName;
        this.entpName = entpName;
        this.chart = chart;
        this.itemImage = itemImage;
    }
}
