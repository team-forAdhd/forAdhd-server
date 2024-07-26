package com.project.foradhd.domain.medicine.web.dto.response;

import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicineSortedResponse {
    private String kind;
    private List<MedicineDto> medicineList;
}
