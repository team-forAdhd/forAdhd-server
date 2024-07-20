package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
public class HospitalDetailsData {

    private Hospital hospital;
    private Boolean isBookmarked;
    private Boolean isEvaluationReviewed;
    private List<Doctor> doctorList;
}
