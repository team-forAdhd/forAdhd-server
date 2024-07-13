package com.project.foradhd.domain.hospital.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DoctorBriefListResponse {

    private List<DoctorBriefResponse> doctorList;

    @Getter
    @Builder
    public static class DoctorBriefResponse {

        private String doctorId;
        private String name;
        private String image;
    }
}
