package com.project.foradhd.domain.medicine.web.dto.request;

import lombok.Getter;

@Getter
public class MedicineFilteringRequest {
    private String itemName;
    private String form;
    private String shape;
    private String color;
}
