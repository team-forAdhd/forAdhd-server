package com.project.foradhd.domain.hospital.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.global.serializer.PhoneSerializer;
import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalDetailsResponse {

    private String hospitalId;
    private String name;
    private String address;
    @JsonSerialize(using = PhoneSerializer.class)
    private String phone;
    private Double latitude;
    private Double longitude;
    private Integer totalReceiptReviewCount;
    private Integer totalEvaluationReviewCount;
    private Double distance;
    private Boolean isBookmarked;
    private Boolean isEvaluationReviewed;
    private HospitalOperationStatus operationStatus;
    private Integer operationStartHour;
    private Integer operationStartMin;
    private Integer operationEndHour;
    private Integer operationEndMin;
    private List<DoctorResponse> doctorList;

    public void synchronizeOperationDetails(HospitalOperationStatus operationStatus, Integer operationStartHour, Integer operationStartMin,
                                            Integer operationEndHour, Integer operationEndMin) {
        this.operationStatus = operationStatus;
        this.operationStartHour = operationStartHour;
        this.operationStartMin = operationStartMin;
        this.operationEndHour = operationEndHour;
        this.operationEndMin = operationEndMin;
    }

    @Getter
    @Builder
    public static class DoctorResponse {

        private String doctorId;
        private String name;
        private String image;
        private Long totalReceiptReviewCount;
        private String profile;
    }
}
