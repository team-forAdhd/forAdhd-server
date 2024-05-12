package com.project.foradhd.domain.hospital.business.dto.out;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
public class HospitalDetailsData {

    private String name;
    private String address;
    private String phone;
    private Double latitude;
    private Double longitude;
    private Boolean isBookmarked;
    private List<DoctorData> doctorList;

    @Getter
    @Builder
    public static class DoctorData {

        private String doctorId;
        private String name;
        private String image;
        private Double totalGrade;
        private Long totalReviewCount;
        private String profile;
    }
}
