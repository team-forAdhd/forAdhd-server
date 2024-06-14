package com.project.foradhd.domain.medicine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineBookmarkRequest {
    private String userId;
    private Long medicineId;
}
