package com.project.foradhd.domain.hospital.business.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.foradhd.domain.hospital.business.dto.in.*;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalEvaluationReviewCreateData.HospitalEvaluationAnswerCreateData;
import com.project.foradhd.domain.hospital.business.dto.out.*;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListBookmarkData.HospitalBookmarkData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListNearbyData.HospitalNearbyData;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBookmarkDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalReceiptReviewDto;
import com.project.foradhd.domain.hospital.persistence.entity.*;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalBookmark.HospitalBookmarkId;
import com.project.foradhd.domain.hospital.persistence.repository.*;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewFilter;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewType;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import com.project.foradhd.global.util.JsonUtil;
import com.project.foradhd.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalBookmarkRepository hospitalBookmarkRepository;
    private final HospitalReceiptReviewRepository hospitalReceiptReviewRepository;
    private final HospitalReceiptReviewHelpRepository hospitalReceiptReviewHelpRepository;
    private final HospitalEvaluationReviewRepository hospitalEvaluationReviewRepository;
    private final HospitalEvaluationQuestionRepository hospitalEvaluationQuestionRepository;
    private final HospitalReviewRepository hospitalReviewRepository;

    public HospitalListNearbyData getHospitalListNearby(String userId, HospitalListNearbySearchCond searchCond,
                                                        Pageable pageable) {
        Page<HospitalNearbyDto> hospitalPaging = hospitalRepository.findAllNearby(userId, searchCond, pageable);
        List<HospitalNearbyData> hospitalList = hospitalPaging.getContent().stream()
                .map(dto -> HospitalNearbyData.builder()
                        .hospital(dto.getHospital())
                        .distance(dto.getDistance())
                        .isBookmarked(dto.isBookmarked())
                        .build())
                .toList();
        PagingResponse paging = PagingResponse.from(hospitalPaging);
        return HospitalListNearbyData.builder()
                .hospitalList(hospitalList)
                .paging(paging)
                .build();
    }

    public HospitalReceiptReviewListData getReceiptReviewList(String userId, String hospitalId, String doctorId, Pageable pageable) {
        Page<HospitalReceiptReviewDto> hospitalReceiptReviewPaging = hospitalReceiptReviewRepository
                .findAll(userId, hospitalId, doctorId, pageable);
        List<HospitalReceiptReviewListData.HospitalReceiptReviewData> hospitalReceiptReviewList = hospitalReceiptReviewPaging.getContent()
                .stream()
                .map(dto -> {
                    HospitalReceiptReview hospitalReceiptReview = dto.getHospitalReceiptReview();
                    UserProfile writerProfile = dto.getUserProfile();
                    Doctor doctor = dto.getDoctor();
                    return HospitalReceiptReviewListData.HospitalReceiptReviewData.builder()
                            .hospitalReceiptReview(hospitalReceiptReview)
                            .writerId(writerProfile.getUser().getId())
                            .writerName(writerProfile.getNickname())
                            .writerImage(writerProfile.getProfileImage())
                            .doctorName(doctor == null ? null : doctor.getName())
                            .isHelped(dto.isHelped())
                            .isMine(dto.isMine())
                            .build();
                }).toList();
        PagingResponse paging = PagingResponse.from(hospitalReceiptReviewPaging);

        return HospitalReceiptReviewListData.builder()
                .hospitalReceiptReviewList(hospitalReceiptReviewList)
                .paging(paging)
                .build();
    }

    public HospitalListBookmarkData getHospitalListBookmark(String userId, HospitalListBookmarkSearchCond searchCond, Pageable pageable) {
        Page<HospitalBookmarkDto> hospitalPaging = hospitalRepository.findAllBookmark(userId, searchCond, pageable);
        List<HospitalBookmarkData> hospitalList = hospitalPaging.getContent().stream()
                .map(dto -> HospitalBookmarkData.builder()
                        .hospital(dto.getHospital())
                        .distance(dto.getDistance())
                        .build())
                .toList();
        PagingResponse paging = PagingResponse.from(hospitalPaging);
        return HospitalListBookmarkData.builder()
                .hospitalList(hospitalList)
                .paging(paging)
                .build();
    }

    public MyHospitalReviewListData getMyHospitalReviewList(String userId, HospitalReviewFilter filter, Pageable pageable) {
        List<MyHospitalReviewListData.MyHospitalReviewData> hospitalReviewList = hospitalReviewRepository.findMyHospitalReviewList(userId, filter, pageable)
                .stream().map(hospitalReview -> MyHospitalReviewListData.MyHospitalReviewData.builder()
                        .hospitalReviewId(hospitalReview.getHospitalReviewId())
                        .hospitalId(hospitalReview.getHospitalId())
                        .hospitalName(hospitalReview.getHospitalName())
                        .reviewType(HospitalReviewType.valueOf(hospitalReview.getReviewType()))
                        .createdAt(TimeUtil.toEpochSecond(hospitalReview.getCreatedAt()))
                        .content(hospitalReview.getContent())
                        .imageList(JsonUtil.readValue(hospitalReview.getImageList(), new TypeReference<>() {}))
                        .build())
                .toList();

        long totalElements = hospitalReviewRepository.countMyHospitalReviewList(userId, filter);
        PagingResponse paging = PagingResponse.from(pageable.getPageNumber(), pageable.getPageSize(),
                hospitalReviewList.size(), totalElements);

        return MyHospitalReviewListData.builder()
                .hospitalReviewList(hospitalReviewList)
                .paging(paging)
                .build();
    }

    public DoctorBriefListData getDoctorBriefList(String hospitalId) {
        List<Doctor> doctorList = doctorRepository.findAllByHospitalIdOrderByName(hospitalId);
        return new DoctorBriefListData(doctorList);
    }

    public HospitalDetailsData getHospitalDetails(String userId, String hospitalId) {
        Hospital hospital = getHospital(hospitalId);
        List<Doctor> doctorList = doctorRepository.findAllByHospitalIdOrderByName(hospitalId);
        HospitalBookmark notHospitalBookmark = HospitalBookmark.builder().deleted(true).build();
        HospitalBookmark hospitalBookmark = hospitalBookmarkRepository.findById(userId, hospitalId)
                .orElse(notHospitalBookmark);
        boolean isEvaluationReviewed = hospitalEvaluationReviewRepository.findByUserIdAndHospitalId(userId, hospitalId).isPresent();
        return HospitalDetailsData.builder()
                .hospital(hospital)
                .isBookmarked(!hospitalBookmark.getDeleted())
                .isEvaluationReviewed(isEvaluationReviewed)
                .doctorList(doctorList)
                .build();
    }

    public Hospital getHospital(String hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL));
    }

    public Doctor getDoctor(String doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DOCTOR));
    }

    public Doctor getDoctor(String hospitalId, String doctorId) {
        return doctorRepository.findByIdAndHospitalId(doctorId, hospitalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DOCTOR));
    }

    public HospitalReceiptReview getHospitalReceiptReview(String hospitalReceiptReviewId) {
        return hospitalReceiptReviewRepository.findById(hospitalReceiptReviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_RECEIPT_REVIEW));
    }

    public HospitalEvaluationReview getHospitalEvaluationReview(String hospitalEvaluationReviewId) {
        return hospitalEvaluationReviewRepository.findById(hospitalEvaluationReviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_EVALUATION_REVIEW));
    }

    public HospitalEvaluationReview getHospitalEvaluationReviewFetch(String hospitalEvaluationReviewId) {
        return hospitalEvaluationReviewRepository.findByIdFetch(hospitalEvaluationReviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_EVALUATION_REVIEW));
    }

    public HospitalEvaluationReview getHospitalEvaluationReviewFetchAll(String hospitalEvaluationReviewId) {
        return hospitalEvaluationReviewRepository.findByIdFetchAll(hospitalEvaluationReviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_EVALUATION_REVIEW));
    }

    public HospitalEvaluationQuestionListData getEvaluationQuestionList() {
        List<HospitalEvaluationQuestion> hospitalEvaluationQuestionList = hospitalEvaluationQuestionRepository.findAllOrderBySeq();
        return new HospitalEvaluationQuestionListData(hospitalEvaluationQuestionList);
    }

    public HospitalEvaluationReviewData getEvaluationReview(String userId, String hospitalEvaluationReviewId) {
        HospitalEvaluationReview hospitalEvaluationReview = getHospitalEvaluationReviewFetchAll(hospitalEvaluationReviewId);
        validateEvaluationReviewer(hospitalEvaluationReview, userId);
        return new HospitalEvaluationReviewData(hospitalEvaluationReview.getHospitalEvaluationAnswerList());
    }

    @Transactional
    public void createEvaluationReview(String userId, String hospitalId,
                                    HospitalEvaluationReviewCreateData hospitalEvaluationReviewCreateData) {
        validateDuplicatedHospitalEvaluationReview(userId, hospitalId);
        List<Long> hospitalEvaluationQuestionIds = hospitalEvaluationReviewCreateData.getHospitalEvaluationAnswerList().stream()
                .map(HospitalEvaluationAnswerCreateData::getHospitalEvaluationQuestionId)
                .toList();
        Map<Long, HospitalEvaluationQuestion> hospitalEvaluationQuestionById = hospitalEvaluationQuestionRepository.findAll().stream()
                .collect(Collectors.toMap(HospitalEvaluationQuestion::getId, identity()));
        validateEvaluationQuestion(hospitalEvaluationQuestionIds, hospitalEvaluationQuestionById);

        User user = User.builder().id(userId).build();
        Hospital hospital = getHospital(hospitalId);
        HospitalEvaluationReview hospitalEvaluationReview = HospitalEvaluationReview.builder()
                .user(user)
                .hospital(hospital)
                .build();
        List<HospitalEvaluationAnswer> hospitalEvaluationAnswerList = hospitalEvaluationReviewCreateData.getHospitalEvaluationAnswerList()
                .stream()
                .map(evaluationAnswer -> HospitalEvaluationAnswer.builder()
                        .hospitalEvaluationQuestion(hospitalEvaluationQuestionById.get(evaluationAnswer.getHospitalEvaluationQuestionId()))
                        .answer(evaluationAnswer.getAnswer())
                        .build())
                .toList();
        hospitalEvaluationReview.updateHospitalEvaluationAnswerList(hospitalEvaluationAnswerList);

        hospitalEvaluationReviewRepository.save(hospitalEvaluationReview);
        int totalEvaluationReviewCount = hospitalEvaluationReviewRepository.countByHospitalId(hospitalId);
        hospital.updateTotalEvaluationReviewCount(totalEvaluationReviewCount);
    }

    @Transactional
    public void updateEvaluationReview(String userId, String hospitalEvaluationReviewId, HospitalEvaluationReviewUpdateData hospitalEvaluationReviewUpdateData) {
        HospitalEvaluationReview hospitalEvaluationReview = getHospitalEvaluationReviewFetch(hospitalEvaluationReviewId);
        validateEvaluationReviewer(hospitalEvaluationReview, userId);
        Map<Long, HospitalEvaluationAnswer> hospitalEvaluationAnswerByQuestionId =
                hospitalEvaluationReview.getHospitalEvaluationAnswerList().stream()
                        .collect(Collectors.toMap(evaluationAnswer -> evaluationAnswer.getHospitalEvaluationQuestion().getId(), identity()));

        hospitalEvaluationReviewUpdateData.getHospitalEvaluationAnswerList().forEach(evaluationAnswer -> {
            Long questionId = evaluationAnswer.getHospitalEvaluationQuestionId();
            Boolean answer = evaluationAnswer.getAnswer();
            if (hospitalEvaluationAnswerByQuestionId.containsKey(questionId)) {
                HospitalEvaluationAnswer hospitalEvaluationAnswer = hospitalEvaluationAnswerByQuestionId.get(questionId);
                hospitalEvaluationAnswer.updateAnswer(answer);
            }
        });
    }

    @Transactional
    public void deleteEvaluationReview(String userId, String hospitalEvaluationReviewId) {
        HospitalEvaluationReview hospitalEvaluationReview = getHospitalEvaluationReviewFetch(hospitalEvaluationReviewId);
        validateEvaluationReviewer(hospitalEvaluationReview, userId);
        String hospitalId = hospitalEvaluationReview.getHospital().getId();
        Hospital hospital = getHospital(hospitalId);

        hospitalEvaluationReviewRepository.delete(hospitalEvaluationReview);
        int totalEvaluationReviewCount = hospitalEvaluationReviewRepository.countByHospitalId(hospitalId);
        hospital.updateTotalEvaluationReviewCount(totalEvaluationReviewCount);
    }

    @Transactional
    public void saveHospitalBookmark(String userId, String hospitalId, boolean bookmark) {
        Hospital hospital = getHospital(hospitalId);
        HospitalBookmark hospitalBookmark = HospitalBookmark.builder()
                .id(new HospitalBookmarkId(
                        User.builder().id(userId).build(), hospital))
                .deleted(!bookmark)
                .build();
        hospitalBookmarkRepository.save(hospitalBookmark);
    }

    @Transactional
    public void createReceiptReview(String userId, String hospitalId,
                                    HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData) {
        validateDuplicatedHospitalReceiptReview(userId, null); //TODO: 영수증 ID로 중복 검증 로직 구현
        Hospital hospital = getHospital(hospitalId);
        HospitalReceiptReview hospitalReceiptReview = HospitalReceiptReview.builder()
                .user(User.builder().id(userId).build())
                .hospital(hospital)
                .content(hospitalReceiptReviewCreateData.getContent())
                .images(hospitalReceiptReviewCreateData.getImageList())
                .medicalExpense(hospitalReceiptReviewCreateData.getMedicalExpense())
                .build();

        hospitalReceiptReviewRepository.save(hospitalReceiptReview);
        int totalHospitalReceiptReviewCount = hospitalReceiptReviewRepository.countByHospitalId(hospitalId);
        hospital.updateTotalReceiptReviewCount(totalHospitalReceiptReviewCount);
    }

    @Transactional
    public void createReceiptReview(String userId, String hospitalId, String doctorId,
                                    HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData) {
        validateDuplicatedHospitalReceiptReview(userId, null); //TODO: 영수증 ID로 중복 검증 로직 구현
        Hospital hospital = getHospital(hospitalId);
        Doctor doctor = getDoctor(hospitalId, doctorId);
        HospitalReceiptReview hospitalReceiptReview = HospitalReceiptReview.builder()
                .user(User.builder().id(userId).build())
                .hospital(hospital)
                .doctor(doctor)
                .content(hospitalReceiptReviewCreateData.getContent())
                .images(hospitalReceiptReviewCreateData.getImageList())
                .medicalExpense(hospitalReceiptReviewCreateData.getMedicalExpense())
                .build();

        hospitalReceiptReviewRepository.save(hospitalReceiptReview);
        int totalHospitalReceiptReviewCount = hospitalReceiptReviewRepository.countByHospitalId(hospitalId);
        int totalDoctorReceiptReviewCount = hospitalReceiptReviewRepository.countByDoctorId(doctorId);
        hospital.updateTotalReceiptReviewCount(totalHospitalReceiptReviewCount);
        doctor.updateTotalReceiptReviewCount(totalDoctorReceiptReviewCount);
    }

    @Transactional
    public void markReceiptReviewAsHelpful(String userId, String hospitalReceiptReviewId, Boolean help) {
        HospitalReceiptReview hospitalReceiptReview = getHospitalReceiptReview(hospitalReceiptReviewId);
        HospitalReceiptReviewHelp hospitalReceiptReviewHelp = HospitalReceiptReviewHelp.builder()
                .id(new HospitalReceiptReviewHelp.HospitalReceiptReviewHelpId(
                        User.builder().id(userId).build(), hospitalReceiptReview))
                .deleted(!help)
                .build();
        hospitalReceiptReviewHelpRepository.save(hospitalReceiptReviewHelp);
        Integer helpCount = hospitalReceiptReviewHelpRepository.countHelp(hospitalReceiptReviewId);
        hospitalReceiptReview.updateHelpCount(helpCount);
    }

    @Transactional
    public void updateReceiptReview(String userId, String hospitalReceiptReviewId,
                                    HospitalReceiptReviewUpdateData hospitalReceiptReviewUpdateData) {
        HospitalReceiptReview hospitalReceiptReview = getHospitalReceiptReview(hospitalReceiptReviewId);
        validateReceiptReviewer(hospitalReceiptReview, userId);
        hospitalReceiptReview.update(hospitalReceiptReviewUpdateData.getContent(),
                hospitalReceiptReviewUpdateData.getImageList(), hospitalReceiptReviewUpdateData.getMedicalExpense());
    }

    @Transactional
    public void deleteReceiptReview(String userId, String hospitalReceiptReviewId) {
        HospitalReceiptReview hospitalReceiptReview = getHospitalReceiptReview(hospitalReceiptReviewId);
        validateReceiptReviewer(hospitalReceiptReview, userId);
        hospitalReceiptReviewRepository.deleteSoftly(hospitalReceiptReviewId);

        String hospitalId = hospitalReceiptReview.getHospital().getId();
        String doctorId = hospitalReceiptReview.getDoctor().getId();
        int totalHospitalReceiptReviewCount = hospitalReceiptReviewRepository.countByHospitalId(hospitalId);
        int totalDoctorReceiptReviewCount = hospitalReceiptReviewRepository.countByDoctorId(doctorId);

        Hospital hospital = getHospital(hospitalId);
        hospital.updateTotalReceiptReviewCount(totalHospitalReceiptReviewCount);
        doctorRepository.findById(doctorId)
                .ifPresent(doctor -> doctor.updateTotalReceiptReviewCount(totalDoctorReceiptReviewCount));
    }

    public void validateDuplicatedHospitalReceiptReview(String userId, String receiptId) {
        boolean existsHospitalReceiptReview = hospitalReceiptReviewRepository.findByUserIdAndReceiptId(userId, receiptId).isPresent();
        if (existsHospitalReceiptReview) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_HOSPITAL_RECEIPT_REVIEW);
        }
    }

    public void validateDuplicatedHospitalEvaluationReview(String userId, String hospitalId) {
        boolean existsHospitalEvaluationReview = hospitalEvaluationReviewRepository.findByUserIdAndHospitalId(userId, hospitalId).isPresent();
        if (existsHospitalEvaluationReview) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_HOSPITAL_EVALUATION_REVIEW);
        }
    }

    public void validateEvaluationQuestion(List<Long> hospitalEvaluationQuestionIds, Map<Long, HospitalEvaluationQuestion> hospitalEvaluationQuestionById) {
        //답변의 평가 질문이 유효한지 검증
        hospitalEvaluationQuestionIds.forEach(hospitalEvaluationQuestionId -> {
            if (!hospitalEvaluationQuestionById.containsKey(hospitalEvaluationQuestionId)) {
                throw new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_EVALUATION_QUESTION);
            }
        });
        //유효한 평가 질문에 대한 답변 존재 여부 검증
        hospitalEvaluationQuestionById.keySet().forEach(hospitalEvaluationQuestionId -> {
            if (!hospitalEvaluationQuestionIds.contains(hospitalEvaluationQuestionId)) {
                throw new BusinessException(ErrorCode.REQUIRED_HOSPITAL_EVALUATION_ANSWER);
            }
        });
    }

    public void validateReceiptReviewer(HospitalReceiptReview hospitalReceiptReview, String userId) {
        String reviewerId = hospitalReceiptReview.getUser().getId();
        if (!Objects.equals(reviewerId, userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_HOSPITAL_RECEIPT_REVIEW);
        }
    }

    public void validateEvaluationReviewer(HospitalEvaluationReview hospitalEvaluationReview, String userId) {
        String reviewerId = hospitalEvaluationReview.getUser().getId();
        if (!Objects.equals(reviewerId, userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_HOSPITAL_EVALUATION_REVIEW);
        }
    }
}
