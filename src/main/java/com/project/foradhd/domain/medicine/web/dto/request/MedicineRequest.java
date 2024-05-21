package com.project.foradhd.domain.medicine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MedicineRequest {
    private String name;
    private String engName;
    private String manufacturer;
    private String images;
}
