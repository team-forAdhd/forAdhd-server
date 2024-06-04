package com.project.foradhd.domain.hospital.web.mapper;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalBriefReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.DoctorDetailsData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListNearbyData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListNearbyData.HospitalNearbyData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalReceiptReviewListData;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalBriefReviewCreateRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalListNearbyRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewCreateRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewUpdateRequest;
import com.project.foradhd.domain.hospital.web.dto.response.DoctorDetailsResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalDetailsResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalListNearbyResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalListNearbyResponse.HospitalNearbyResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalReceiptReviewListResponse;
import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface HospitalMapper {

    HospitalListNearbySearchCond mapToSearchCond(HospitalListNearbyRequest request);

    @Mappings({
            @Mapping(target = "hospitalList", source = "hospitalList", qualifiedByName = "mapToHospitalList")
    })
    HospitalListNearbyResponse toHospitalListNearbyResponse(HospitalListNearbyData hospitalListNearbyData);

    @Named("mapToHospitalList")
    @IterableMapping(qualifiedByName = "mapToHospital")
    List<HospitalNearbyResponse> mapToHospitalList(List<HospitalNearbyData> hospitalList);

    @Named("mapToHospital")
    HospitalNearbyResponse mapToHospital(HospitalNearbyData hospital);

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

    @Mappings({
            @Mapping(target = "receiptReviewList", source = "receiptReviewList", qualifiedByName = "mapToReceiptReviewList")
    })
    HospitalReceiptReviewListResponse toReceiptReviewListResponse(HospitalReceiptReviewListData hospitalReceiptReviewListData);

    @Named("mapToReceiptReviewList")
    @IterableMapping(qualifiedByName = "mapToReceiptReview")
    List<HospitalReceiptReviewListResponse.ReceiptReviewResponse>
        mapToReceiptReviewList(List<HospitalReceiptReviewListData.ReceiptReviewData> receiptreviewList);

    @Named("mapToReceiptReview")
    HospitalReceiptReviewListResponse.ReceiptReviewResponse
        mapToReceiptReview(HospitalReceiptReviewListData.ReceiptReviewData receiptReview);

    HospitalBriefReviewCreateData toHospitalBriefReviewCreateData(HospitalBriefReviewCreateRequest request);

    HospitalReceiptReviewCreateData toHospitalReceiptReviewCreateData(HospitalReceiptReviewCreateRequest request);

    HospitalReceiptReviewUpdateData toHospitalReceiptReviewUpdateData(HospitalReceiptReviewUpdateRequest request);
}
