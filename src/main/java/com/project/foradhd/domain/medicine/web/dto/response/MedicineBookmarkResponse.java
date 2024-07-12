package com.project.foradhd.domain.medicine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineBookmarkResponse {
    private Long id;
    private String name;
    private String engName;
    private String manufacturer;
    private String images;
}
