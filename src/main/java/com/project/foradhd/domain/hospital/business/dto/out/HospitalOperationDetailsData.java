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

    private HospitalOperationStatus operationStatus = HospitalOperationStatus.UNKNOWN;
    private int operationStartHour;
    private int operationStartMin;
    private int operationEndHour;
    private int operationEndMin;
}
