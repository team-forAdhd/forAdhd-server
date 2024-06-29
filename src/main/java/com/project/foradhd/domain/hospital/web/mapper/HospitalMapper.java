package com.project.foradhd.domain.hospital.web.mapper;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalEvaluationReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.*;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListNearbyData.HospitalNearbyData;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationAnswer;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationQuestion;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalEvaluationReviewCreateRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalListNearbyRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewCreateRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewUpdateRequest;
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
}
