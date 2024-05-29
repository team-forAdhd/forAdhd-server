package com.project.foradhd.domain.hospital.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.hospital.web.dto.response.serializer.PhoneSerializer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalDetailsResponse {

    private String name;
    private String address;
    @JsonSerialize(using = PhoneSerializer.class)
    private String phone;
    private Double latitude;
    private Double longitude;
    private Boolean isBookmarked;
    private List<DoctorResponse> doctorList;

    @Getter
    @Builder
    public static class DoctorResponse {

        private String doctorId;
        private String name;
        private String image;
        private Double totalGrade;
        private Long totalReviewCount;
        private String profile;
    }
}
