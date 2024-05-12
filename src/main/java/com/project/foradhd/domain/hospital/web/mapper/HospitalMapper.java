package com.project.foradhd.domain.hospital.web.mapper;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalBriefReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.DoctorDetailsData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReview;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalBriefReviewCreateRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewCreateRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewUpdateRequest;
import com.project.foradhd.domain.hospital.web.dto.response.DoctorDetailsResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalDetailsResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalReceiptReviewListResponse;
import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface HospitalMapper {

    @Mappings({
            @Mapping(target = "doctorList", source = "doctorList", qualifiedByName = "mapToDoctorList")
    })
    HospitalDetailsResponse toHospitalDetailsResponse(HospitalDetailsData hospitalDetailsData);

    @Named("mapToDoctorList")
    @IterableMapping(qualifiedByName = "mapToDoctor")
    List<HospitalDetailsResponse.DoctorResponse> mapToDoctorList(List<HospitalDetailsData.DoctorData> doctorList);

    @Named("mapToDoctor")
    HospitalDetailsResponse.DoctorResponse mapToDoctor(HospitalDetailsData.DoctorData doctor);

    DoctorDetailsResponse toDoctorDetailsResponse(DoctorDetailsData doctorDetailsData);

    default HospitalReceiptReviewListResponse toReceiptReviewListResponse(List<HospitalReceiptReview> receiptReviewList) {
        return null;
    }

    HospitalBriefReviewCreateData toHospitalBriefReviewCreateData(HospitalBriefReviewCreateRequest request);

    HospitalReceiptReviewCreateData toHospitalReceiptReviewCreateData(HospitalReceiptReviewCreateRequest request);

    default HospitalReceiptReviewUpdateData toHospitalReceiptReviewUpdateData(HospitalReceiptReviewUpdateRequest request) {
        return null;
    }
}
