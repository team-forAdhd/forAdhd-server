package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.out.HospitalOperationDetailsData;
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
                    GooglePlaceOpeningHoursResponse response = googlePlacesClient.getPlaceOpeningHours(placeId);
                    return getOpenNow(response)
                            .map(HospitalOperationStatus::from)
                            .orElse(HospitalOperationStatus.UNKNOWN);
                }));
    }

    public HospitalOperationDetailsData getHospitalOperationDetails(String placeId) {
        GooglePlaceOpeningHoursResponse response = googlePlacesClient.getPlaceOpeningHours(placeId);
        HospitalOperationStatus operationStatus = getOpenNow(response)
                .map(HospitalOperationStatus::from)
                .orElse(HospitalOperationStatus.UNKNOWN);
        GooglePlaceOpeningHoursResponse.Period todayOpeningPeriod = getTodayOpeningPeriod(response);
        return HospitalOperationDetailsData.builder()
                .operationStatus(operationStatus)
                .operationStartHour(todayOpeningPeriod.getOpen().getHour())
                .operationStartMin(todayOpeningPeriod.getOpen().getMinute())
                .operationEndHour(todayOpeningPeriod.getClose().getHour())
                .operationEndMin(todayOpeningPeriod.getClose().getMinute())
                .build();
    }

    private GooglePlaceOpeningHoursResponse.Period getTodayOpeningPeriod(GooglePlaceOpeningHoursResponse response) {
        if (response.getCurrentOpeningHours() == null) return new GooglePlaceOpeningHoursResponse.Period();
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

    private Optional<Boolean> getOpenNow(GooglePlaceOpeningHoursResponse response) {
        if (response.getCurrentOpeningHours() == null) return Optional.empty();
        return Optional.of(response.getCurrentOpeningHours().isOpenNow());
    }
}
