package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import com.project.foradhd.global.client.GooglePlacesClient;
import com.project.foradhd.global.client.dto.response.GooglePlaceOpeningHoursResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HospitalOperationService {

    private final GooglePlacesClient googlePlacesClient;

    public Map<String, HospitalOperationStatus> getHospitalOperationStatus(Map<String, String> placeIdByHospital) {
        return placeIdByHospital.keySet().stream()
                .collect(Collectors.toMap(hospitalId -> hospitalId, hospitalId -> {
                    String placeId = placeIdByHospital.get(hospitalId);
                    GooglePlaceOpeningHoursResponse operationDetails = googlePlacesClient.getPlaceOpeningHours(placeId);
                    return getOpenNow(operationDetails)
                            .map(HospitalOperationStatus::from)
                            .orElse(HospitalOperationStatus.UNKNOWN);
                }));
    }

    public Optional<Boolean> getOpenNow(GooglePlaceOpeningHoursResponse response) {
        if (response.getCurrentOpeningHours() == null) return Optional.empty();
        return Optional.of(response.getCurrentOpeningHours().isOpenNow());
    }

    public GooglePlaceOpeningHoursResponse.Period getTodayOpeningPeriod(GooglePlaceOpeningHoursResponse response) {
        LocalDate now = LocalDate.now();
        return response.getCurrentOpeningHours().getPeriods().stream()
                .filter(period -> {
                    GooglePlaceOpeningHoursResponse.Date date = period.getOpen().getDate();
                    return date.getYear() == now.getYear() &&
                            date.getMonth() == now.getMonthValue() &&
                            date.getDay() == now.getDayOfMonth();
                })
                .findFirst()
                .orElse(new GooglePlaceOpeningHoursResponse.Period());
    }
}
