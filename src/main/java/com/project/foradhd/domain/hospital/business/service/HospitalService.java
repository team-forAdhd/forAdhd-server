package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalBriefReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.DoctorDetailsData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBriefReviewSummary;
import com.project.foradhd.domain.hospital.persistence.entity.*;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalBookmark.HospitalBookmarkId;
import com.project.foradhd.domain.hospital.persistence.repository.*;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.project.foradhd.global.util.AverageCalculator.calculateAverage;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalBookmarkRepository hospitalBookmarkRepository;
    private final HospitalReceiptReviewRepository hospitalReceiptReviewRepository;
    private final HospitalBriefReviewRepository hospitalBriefReviewRepository;

    public HospitalDetailsData getHospitalDetails(String userId, String hospitalId) {
        Hospital hospital = getHospital(hospitalId);
        List<Doctor> doctorList = doctorRepository.findAllByHospitalId(hospitalId);
        HospitalBookmark notHospitalBookmark = HospitalBookmark.builder().deleted(true).build();
        HospitalBookmark hospitalBookmark = hospitalBookmarkRepository.findById(userId, hospitalId)
                .orElse(notHospitalBookmark);

        List<HospitalDetailsData.DoctorData> doctorDataList = doctorList.stream()
                .map(doctor -> HospitalDetailsData.DoctorData.builder()
                        .doctorId(doctor.getId())
                        .name(doctor.getName())
                        .image(doctor.getImage())
                        .totalGrade(doctor.calculateTotalGrade())
                        .totalReviewCount(doctor.calculateTotalReviewCount())
                        .profile(doctor.getProfile())
                        .build())
                .toList();
        return HospitalDetailsData.builder()
                .name(hospital.getName())
                .address(hospital.getAddress())
                .phone(hospital.getPhone())
                .latitude(hospital.getLocation().getY())
                .longitude(hospital.getLocation().getX())
                .isBookmarked(!hospitalBookmark.getDeleted())
                .doctorList(doctorDataList)
                .build();
    }

    public Hospital getHospital(String hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL));
    }

    public DoctorDetailsData getDoctorDetails(String hospitalId, String doctorId) {
        Doctor doctor = getDoctor(hospitalId, doctorId);
        HospitalBriefReviewSummary briefReviewSummary = hospitalBriefReviewRepository.getSummaryByDoctorId(doctorId);
        Long totalBriefReviewCount = briefReviewSummary.getTotalBriefReviewCount();

        DoctorDetailsData.BriefReviewData briefReviewData = DoctorDetailsData.BriefReviewData.builder()
                .totalReviewCount(totalBriefReviewCount)
                .kindness(calculateAverage(briefReviewSummary.getTotalKindnessSum(), totalBriefReviewCount))
                .adhdUnderstanding(calculateAverage(briefReviewSummary.getTotalAdhdUnderstandingSum(), totalBriefReviewCount))
                .enoughMedicalTime(calculateAverage(briefReviewSummary.getTotalEnoughMedicalTimeSum(), totalBriefReviewCount))
                .build();
        return DoctorDetailsData.builder()
                .name(doctor.getName())
                .totalGrade(doctor.calculateTotalGrade())
                .totalReviewCount(doctor.calculateTotalReviewCount())
                .profile(doctor.getProfile())
                .briefReview(briefReviewData)
                .build();
    }

    public Doctor getDoctor(String doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DOCTOR));
    }

    public Doctor getDoctor(String hospitalId, String doctorId) {
        return doctorRepository.findByIdAndHospitalId(doctorId, hospitalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DOCTOR));
    }

    public HospitalBriefReview getHospitalBriefReview(String hospitalBriefReviewId) {
        return hospitalBriefReviewRepository.findById(hospitalBriefReviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_BRIEF_REVIEW));
    }

    public HospitalReceiptReview getHospitalReceiptReview(String hospitalReceiptReviewId) {
        return hospitalReceiptReviewRepository.findById(hospitalReceiptReviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_RECEIPT_REVIEW));
    }

    public List<HospitalReceiptReview> getReceiptReviewList(String hospitalId, String doctorId) {
        return null;
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
    public void createBriefReview(HospitalBriefReviewCreateData hospitalBriefReviewCreateData) {

    }

    @Transactional
    public void deleteBriefReview(String userId, String hospitalBriefReviewId) {
        HospitalBriefReview hospitalBriefReview = getHospitalBriefReview(hospitalBriefReviewId);
        validateBriefReviewer(hospitalBriefReview, userId);
        Doctor doctor = getDoctor(hospitalBriefReview.getDoctor().getId());

        Integer briefReviewTotalGradeSum = hospitalBriefReview.calculateTotalGradeSum();
        doctor.updateByDeletedBriefReview(briefReviewTotalGradeSum);
        hospitalBriefReviewRepository.deleteSoftly(hospitalBriefReviewId);
    }

    @Transactional
    public void createReceiptReview(HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData) {

    }

    @Transactional
    public void markReceiptReviewAsHelpful(String hospitalReceiptReviewId, Boolean help) {

    }

    @Transactional
    public void updateReceiptReview(String hospitalReceiptReviewId,
                                    HospitalReceiptReviewUpdateData hospitalReceiptReviewUpdateData) {

    }

    @Transactional
    public void deleteReceiptReview(String userId, String hospitalReceiptReviewId) {
        HospitalReceiptReview hospitalReceiptReview = getHospitalReceiptReview(hospitalReceiptReviewId);
        validateReceiptReviewer(hospitalReceiptReview, userId);
        Doctor doctor = getDoctor(hospitalReceiptReview.getDoctor().getId());

        Integer receiptReviewTotalGradeSum = hospitalReceiptReview.calculateTotalGradeSum();
        doctor.updateByDeletedReceiptReview(receiptReviewTotalGradeSum);
        hospitalReceiptReviewRepository.deleteSoftly(hospitalReceiptReviewId);
    }

    public void validateBriefReviewer(HospitalBriefReview hospitalBriefReview, String userId) {
        String reviewerId = hospitalBriefReview.getUser().getId();
        if (!Objects.equals(reviewerId, userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_HOSPITAL_BRIEF_REVIEW);
        }
    }

    public void validateReceiptReviewer(HospitalReceiptReview hospitalReceiptReview, String userId) {
        String reviewerId = hospitalReceiptReview.getUser().getId();
        if (!Objects.equals(reviewerId, userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_HOSPITAL_RECEIPT_REVIEW);
        }
    }
}
