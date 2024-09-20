package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.out.HospitalOperationDetailsData;
import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;

import java.util.Map;

public interface HospitalOperationService {

    Map<String, HospitalOperationStatus> getHospitalOperationStatus(Map<String, String> placeIdByHospital);

    HospitalOperationDetailsData getHospitalOperationDetails(String placeId);
}
