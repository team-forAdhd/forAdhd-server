package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.out.HospitalOperationDetailsData;
import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import com.project.foradhd.global.client.GooglePlacesClient;
import com.project.foradhd.global.client.dto.response.GooglePlaceOpeningHoursResponse;
import com.project.foradhd.global.enums.RedisKeyType;
import com.project.foradhd.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HospitalOperationService {

    private final GooglePlacesClient googlePlacesClient;
    private final RedisService redisService;

    public Map<String, HospitalOperationStatus> getHospitalOperationStatus(Map<String, String> placeIdByHospital) {
        return placeIdByHospital.keySet().stream()
                .collect(Collectors.toMap(hospitalId -> hospitalId, hospitalId -> {
                    String placeId = placeIdByHospital.get(hospitalId);
                    return readHospitalOperationDetails(placeId)
                            .map(HospitalOperationDetailsData::getOperationStatus)
                            .orElseGet(() -> {
                                HospitalOperationDetailsData hospitalOperationDetails = requestHospitalOperationDetails(placeId);
                                writeHospitalOperationDetails(hospitalOperationDetails, placeId);
                                return hospitalOperationDetails.getOperationStatus();
                            });
                }));
    }

    public HospitalOperationDetailsData getHospitalOperationDetails(String placeId) {
        if (!StringUtils.hasText(placeId)) return new HospitalOperationDetailsData();
        return readHospitalOperationDetails(placeId)
                .orElseGet(() -> {
                    HospitalOperationDetailsData hospitalOperationDetails = requestHospitalOperationDetails(placeId);
                    writeHospitalOperationDetails(hospitalOperationDetails, placeId);
                    return hospitalOperationDetails;
                });
    }

    private HospitalOperationDetailsData requestHospitalOperationDetails(String placeId) {
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

    private Optional<HospitalOperationDetailsData> readHospitalOperationDetails(String placeId) {
        return redisService.getValue(RedisKeyType.HOSPITAL_OPERATION_DETAILS, placeId, HospitalOperationDetailsData.class);
    }

    private void writeHospitalOperationDetails(HospitalOperationDetailsData hospitalOperationDetails, String placeId) {
        long timeout = calculateTimeout(hospitalOperationDetails);
        redisService.setValue(RedisKeyType.HOSPITAL_OPERATION_DETAILS, placeId, hospitalOperationDetails, timeout, TimeUnit.SECONDS);
    }

    private long calculateTimeout(HospitalOperationDetailsData hospitalOperationDetails) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime operationStartTime = now.withHour(hospitalOperationDetails.getOperationStartHour())
                .withMinute(hospitalOperationDetails.getOperationStartMin())
                .withSecond(0);
        LocalDateTime operationEndTime = now.withHour(hospitalOperationDetails.getOperationEndHour())
                .withMinute(hospitalOperationDetails.getOperationEndMin())
                .withSecond(0);
        if (now.isBefore(operationStartTime)) return Duration.between(now, operationStartTime).getSeconds();
        else if (now.isBefore(operationEndTime)) return Duration.between(now, operationEndTime).getSeconds();
        return Duration.between(now, operationStartTime.plusDays(1)).getSeconds();
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
