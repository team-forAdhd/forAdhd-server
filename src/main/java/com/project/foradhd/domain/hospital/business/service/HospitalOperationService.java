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
                    return googlePlacesClient.getPlaceOpeningHours(placeId)
                            .filter(response -> response.getCurrentOpeningHours() != null)
                            .map(this::getOpenNow)
                            .map(HospitalOperationStatus::from)
                            .orElse(HospitalOperationStatus.UNKNOWN);
                }));
    }

    public HospitalOperationDetailsData getHospitalOperationDetails(String placeId) {
        Optional<GooglePlaceOpeningHoursResponse> googlePlaceOpeningHoursResponseOptional =
                googlePlacesClient.getPlaceOpeningHours(placeId);

        HospitalOperationStatus operationStatus = googlePlaceOpeningHoursResponseOptional
                .filter(response -> response.getCurrentOpeningHours() != null)
                .map(this::getOpenNow)
                .map(HospitalOperationStatus::from)
                .orElse(HospitalOperationStatus.UNKNOWN);
        GooglePlaceOpeningHoursResponse.Period todayOpeningPeriod = googlePlaceOpeningHoursResponseOptional
                .filter(response -> response.getCurrentOpeningHours() != null)
                .map(this::getTodayOpeningPeriod)
                .orElse(new GooglePlaceOpeningHoursResponse.Period());
        return HospitalOperationDetailsData.builder()
                .operationStatus(operationStatus)
                .operationStartHour(todayOpeningPeriod.getOpen().getHour())
                .operationStartMin(todayOpeningPeriod.getOpen().getMinute())
                .operationEndHour(todayOpeningPeriod.getClose().getHour())
                .operationEndMin(todayOpeningPeriod.getClose().getMinute())
                .build();
    }

    private GooglePlaceOpeningHoursResponse.Period getTodayOpeningPeriod(GooglePlaceOpeningHoursResponse response) {
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

    private boolean getOpenNow(GooglePlaceOpeningHoursResponse response) {
        return response.getCurrentOpeningHours().isOpenNow();
    }
}
