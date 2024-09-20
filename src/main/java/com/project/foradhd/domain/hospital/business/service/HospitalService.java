package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.in.*;
import com.project.foradhd.domain.hospital.business.dto.out.*;
import com.project.foradhd.domain.hospital.persistence.entity.*;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface HospitalService {

    HospitalListNearbyData getHospitalListNearby(String userId, HospitalListNearbySearchCond searchCond, Pageable pageable);

    HospitalReceiptReviewListData getReceiptReviewList(String userId, String hospitalId, String doctorId, Pageable pageable);

    HospitalListBookmarkData getHospitalListBookmark(String userId, Pageable pageable);

    MyHospitalReviewListData getMyHospitalReviewList(String userId, HospitalReviewFilter filter, Pageable pageable);

    DoctorBriefListData getDoctorBriefList(String hospitalId);

    HospitalDetailsData getHospitalDetails(String userId, String hospitalId);

    Hospital getHospital(String hospitalId);

    Doctor getDoctor(String doctorId);

    Doctor getDoctor(String hospitalId, String doctorId);

    HospitalReceiptReview getHospitalReceiptReview(String hospitalReceiptReviewId);

    HospitalEvaluationReview getHospitalEvaluationReview(String hospitalEvaluationReviewId);

    HospitalEvaluationReview getHospitalEvaluationReviewFetch(String hospitalEvaluationReviewId);

    HospitalEvaluationReview getHospitalEvaluationReviewFetchAll(String hospitalEvaluationReviewId);

    HospitalEvaluationQuestionListData getEvaluationQuestionList();

    HospitalEvaluationReviewData getEvaluationReview(String userId, String hospitalEvaluationReviewId);

    void createEvaluationReview(String userId, String hospitalId,
                                HospitalEvaluationReviewCreateData hospitalEvaluationReviewCreateData);

    void updateEvaluationReview(String userId, String hospitalEvaluationReviewId,
                                HospitalEvaluationReviewUpdateData hospitalEvaluationReviewUpdateData);

    void deleteEvaluationReview(String userId, String hospitalEvaluationReviewId);

    void saveHospitalBookmark(String userId, String hospitalId, boolean bookmark);

    void createReceiptReview(String userId, String hospitalId,
                            HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData);

    void createReceiptReview(String userId, String hospitalId, String doctorId,
                            HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData);

    void markReceiptReviewAsHelpful(String userId, String hospitalReceiptReviewId, Boolean help);

    void updateReceiptReview(String userId, String hospitalReceiptReviewId,
                            HospitalReceiptReviewUpdateData hospitalReceiptReviewUpdateData);

    void deleteReceiptReview(String userId, String hospitalReceiptReviewId);

    void validateDuplicatedHospitalReceiptReview(String userId, String receiptId);

    void validateDuplicatedHospitalEvaluationReview(String userId, String hospitalId);

    void validateEvaluationQuestion(List<Long> hospitalEvaluationQuestionIds,
                                    Map<Long, HospitalEvaluationQuestion> hospitalEvaluationQuestionById);

    void validateReceiptReviewer(HospitalReceiptReview hospitalReceiptReview, String userId);

    void validateEvaluationReviewer(HospitalEvaluationReview hospitalEvaluationReview, String userId);
}
