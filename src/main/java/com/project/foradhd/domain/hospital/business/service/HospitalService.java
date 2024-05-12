package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalBriefReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.DoctorDetailsData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBriefReviewSummary;
import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalBookmark;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReview;
import com.project.foradhd.domain.hospital.persistence.repository.DoctorRepository;
import com.project.foradhd.domain.hospital.persistence.repository.HospitalBookmarkRepository;
import com.project.foradhd.domain.hospital.persistence.repository.HospitalBriefReviewRepository;
import com.project.foradhd.domain.hospital.persistence.repository.HospitalRepository;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalBriefReviewRepository hospitalBriefReviewRepository;
    private final HospitalBookmarkRepository hospitalBookmarkRepository;

    public HospitalDetailsData getHospitalDetails(String userId, String hospitalId) {
        Hospital hospital = getHospitalFetch(hospitalId);
        HospitalBookmark notHospitalBookmark = HospitalBookmark.builder().deleted(true).build();
        HospitalBookmark hospitalBookmark = hospitalBookmarkRepository.findById(userId, hospitalId)
                .orElse(notHospitalBookmark);

        List<HospitalDetailsData.DoctorData> doctorList = hospital.getDoctorList().stream()
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
                .doctorList(doctorList)
                .build();
    }

    public Hospital getHospitalFetch(String hospitalId) {
        return hospitalRepository.findByIdFetch(hospitalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL));
    }

    public DoctorDetailsData getDoctorDetails(String hospitalId, String doctorId) {
        Doctor doctor = getDoctor(hospitalId, doctorId);
        HospitalBriefReviewSummary briefReviewSummary = hospitalBriefReviewRepository.getSummaryByDoctorId(doctorId);
        Long totalBriefReviewCount = briefReviewSummary.getTotalBriefReviewCount();

        DoctorDetailsData.BriefReviewData briefReview = DoctorDetailsData.BriefReviewData.builder()
                .totalReviewCount(totalBriefReviewCount)
                .kindness((double) briefReviewSummary.getTotalKindness() / totalBriefReviewCount)
                .adhdUnderstanding((double) briefReviewSummary.getTotalAdhdUnderstanding() / totalBriefReviewCount)
                .enoughMedicalTime((double) briefReviewSummary.getTotalEnoughMedicalTime() / totalBriefReviewCount)
                .build();
        return DoctorDetailsData.builder()
                .name(doctor.getName())
                .totalGrade(doctor.calculateTotalGrade())
                .totalReviewCount(doctor.calculateTotalReviewCount())
                .profile(doctor.getProfile())
                .briefReview(briefReview)
                .build();
    }

    public Doctor getDoctor(String hospitalId, String doctorId) {
        return doctorRepository.findByIdAndHospitalId(doctorId, hospitalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DOCTOR));
    }

    public List<HospitalReceiptReview> getReceiptReviewList(String hospitalId, String doctorId) {
        return null;
    }

    @Transactional
    public void bookmarkHospital(String hospitalId, Boolean bookmark) {

    }

    @Transactional
    public void createBriefReview(HospitalBriefReviewCreateData hospitalBriefReviewCreateData) {

    }

    @Transactional
    public void deleteBriefReview(String hospitalBriefReviewId) {

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
    public void deleteReceiptReview(String hospitalReceiptReviewId) {

    }
}
