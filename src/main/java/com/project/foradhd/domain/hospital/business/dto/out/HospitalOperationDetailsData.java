package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalOperationDetailsData {

    private HospitalOperationStatus operationStatus;
    private Integer operationStartHour;
    private Integer operationStartMin;
    private Integer operationEndHour;
    private Integer operationEndMin;
}
