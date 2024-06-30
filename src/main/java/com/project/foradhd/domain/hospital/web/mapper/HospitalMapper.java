package com.project.foradhd.domain.hospital.web.mapper;

import com.project.foradhd.domain.hospital.business.dto.in.*;
import com.project.foradhd.domain.hospital.business.dto.out.*;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListNearbyData.HospitalNearbyData;
import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationAnswer;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationQuestion;
import com.project.foradhd.domain.hospital.web.dto.request.*;
import com.project.foradhd.domain.hospital.web.dto.response.*;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalListNearbyResponse.HospitalNearbyResponse;
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

    @Mappings({
            @Mapping(target = "hospitalId", source = "hospital.id"),
            @Mapping(target = "name", source = "hospital.name"),
            @Mapping(target = "totalReceiptReviewCount", source = "hospital.totalReceiptReviewCount"),
            @Mapping(target = "totalEvaluationReviewCount", source = "hospital.totalEvaluationReviewCount"),
            @Mapping(target = "latitude", source = "hospital.location.y"),
            @Mapping(target = "longitude", source = "hospital.location.x")
    })
    @Named("mapToHospital")
    HospitalNearbyResponse mapToHospital(HospitalNearbyData hospital);

    @Mapping(target = "doctorList", source = "doctorList", qualifiedByName = "mapToDoctorBriefList")
    DoctorBriefListResponse toDoctorBriefListResponse(DoctorBriefListData doctorBriefListData);

    @Named("mapToDoctorBriefList")
    @IterableMapping(qualifiedByName = "mapToDoctorBrief")
    List<DoctorBriefListResponse.DoctorBriefResponse> mapToDoctorBriefList(List<Doctor> doctorList);

    @Mapping(target = "doctorId", source = "id")
    @Named("mapToDoctorBrief")
    DoctorBriefListResponse.DoctorBriefResponse mapToDoctorBrief(Doctor doctor);

    @Mappings({
            @Mapping(target = "hospitalId", source = "hospital.id"),
            @Mapping(target = "name", source = "hospital.name"),
            @Mapping(target = "address", source = "hospital.address"),
            @Mapping(target = "phone", source = "hospital.phone"),
            @Mapping(target = "latitude", source = "hospital.location.y"),
            @Mapping(target = "longitude", source = "hospital.location.x"),
            @Mapping(target = "totalReceiptReviewCount", source = "hospital.totalReceiptReviewCount"),
            @Mapping(target = "totalEvaluationReviewCount", source = "hospital.totalEvaluationReviewCount"),
            @Mapping(target = "doctorList", source = "doctorList", qualifiedByName = "mapToDoctorList")
    })
    HospitalDetailsResponse toHospitalDetailsResponse(HospitalDetailsData hospitalDetailsData);

    @Named("mapToDoctorList")
    @IterableMapping(qualifiedByName = "mapToDoctor")
    List<HospitalDetailsResponse.DoctorResponse> mapToDoctorList(List<Doctor> doctorList);

    @Mapping(target = "doctorId", source = "id")
    @Named("mapToDoctor")
    HospitalDetailsResponse.DoctorResponse mapToDoctor(Doctor doctor);

    @Mappings({
            @Mapping(target = "hospitalReceiptReviewList", source = "hospitalReceiptReviewList",
                    qualifiedByName = "mapToHospitalReceiptReviewList")
    })
    HospitalReceiptReviewListResponse toReceiptReviewListResponse(HospitalReceiptReviewListData hospitalReceiptReviewListData);

    @Named("mapToHospitalReceiptReviewList")
    @IterableMapping(qualifiedByName = "mapToHospitalReceiptReview")
    List<HospitalReceiptReviewListResponse.HospitalReceiptReviewResponse>
        mapToHospitalReceiptReviewList(List<HospitalReceiptReviewListData.HospitalReceiptReviewData> receiptreviewList);

    @Mappings({
            @Mapping(target = "hospitalReceiptReviewId", source = "hospitalReceiptReview.id"),
            @Mapping(target = "createdAt", source = "hospitalReceiptReview.createdAt"),
            @Mapping(target = "content", source = "hospitalReceiptReview.content"),
            @Mapping(target = "imageList", source = "hospitalReceiptReview.images"),
            @Mapping(target = "medicalExpense", source = "hospitalReceiptReview.medicalExpense"),
            @Mapping(target = "helpCount", source = "hospitalReceiptReview.helpCount")
    })
    @Named("mapToHospitalReceiptReview")
    HospitalReceiptReviewListResponse.HospitalReceiptReviewResponse
        mapToHospitalReceiptReview(HospitalReceiptReviewListData.HospitalReceiptReviewData receiptReview);

    HospitalReceiptReviewCreateData toHospitalReceiptReviewCreateData(HospitalReceiptReviewCreateRequest request);

    HospitalReceiptReviewUpdateData toHospitalReceiptReviewUpdateData(HospitalReceiptReviewUpdateRequest request);

    @Mappings({
            @Mapping(target = "hospitalEvaluationQuestionList", source = "hospitalEvaluationQuestionList",
                    qualifiedByName = "mapToEvaluationQuestionList")
    })
    HospitalEvaluationQuestionListResponse toEvaluationQuestionListResponse(HospitalEvaluationQuestionListData hospitalEvaluationQuestionList);

    @Named("mapToEvaluationQuestionList")
    @IterableMapping(qualifiedByName = "mapToEvaluationQuestion")
    List<HospitalEvaluationQuestionListResponse.HospitalEvaluationQuestionResponse>
        mapToEvaluationQuestionList(List<HospitalEvaluationQuestion> evaluationQuestionList);

    @Named("mapToEvaluationQuestion")
    @Mapping(target = "hospitalEvaluationQuestionId", source = "id")
    HospitalEvaluationQuestionListResponse.HospitalEvaluationQuestionResponse
        mapToEvaluationQuestion(HospitalEvaluationQuestion evaluationQuestion);

    @Mappings({
            @Mapping(target = "hospitalEvaluationAnswerList", source = "hospitalEvaluationAnswerList",
                    qualifiedByName = "mapToEvaluationAnswerList")
    })
    HospitalEvaluationReviewResponse toEvaluationReviewResponse(HospitalEvaluationReviewData hospitalEvaluationReview);

    @Named("mapToEvaluationAnswerList")
    @IterableMapping(qualifiedByName = "mapToEvaluationAnswer")
    List<HospitalEvaluationReviewResponse.HospitalEvaluationAnswerResponse>
        mapToEvaluationAnswerList(List<HospitalEvaluationAnswer> evaluationAnswerList);

    @Named("mapToEvaluationAnswer")
    @Mappings({
            @Mapping(target = "hospitalEvaluationQuestionId", source = "hospitalEvaluationQuestion.id"),
            @Mapping(target = "seq", source = "hospitalEvaluationQuestion.seq"),
            @Mapping(target = "question", source = "hospitalEvaluationQuestion.question")
    })
    HospitalEvaluationReviewResponse.HospitalEvaluationAnswerResponse
        mapToEvaluationAnswer(HospitalEvaluationAnswer evaluationAnswer);

    HospitalEvaluationReviewCreateData toHospitalEvaluationReviewCreateData(HospitalEvaluationReviewCreateRequest request);

    HospitalEvaluationReviewUpdateData toHospitalEvaluationReviewUpdateData(HospitalEvaluationReviewUpdateRequest request);
}
