package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.DoctorDetailsData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListNearbyData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalListNearbyData.HospitalNearbyData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalReceiptReviewListData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalReceiptReviewListData.ReceiptReviewData;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalReceiptReviewDto;
import com.project.foradhd.domain.hospital.persistence.entity.*;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalBookmark.HospitalBookmarkId;
import com.project.foradhd.domain.hospital.persistence.repository.*;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final HospitalReceiptReviewHelpRepository hospitalReceiptReviewHelpRepository;

    public HospitalListNearbyData getHospitalListNearby(String userId, HospitalListNearbySearchCond searchCond,
                                                        Pageable pageable) {
        Page<HospitalNearbyDto> hospitalPaging = hospitalRepository.findAllNearby(userId, searchCond, pageable);
        List<HospitalNearbyData> hospitalList = hospitalPaging.getContent().stream()
                .map(dto -> {
                    Hospital hospital = dto.getHospital();
                    long totalGradeSum = dto.getTotalGradeSum();
                    int totalReviewCount = dto.getTotalBriefReviewCount() + dto.getTotalReceiptReviewCount();
                    return HospitalNearbyData.builder()
                            .hospitalId(hospital.getId())
                            .name(hospital.getName())
                            .totalGrade(calculateAverage(totalGradeSum, totalReviewCount * 3L))
                            .totalReviewCount(totalReviewCount)
                            .latitude(hospital.getLocation().getY())
                            .longitude(hospital.getLocation().getX())
                            .distance(dto.getDistance())
                            .isBookmarked(dto.isBookmarked())
                            .build();
                })
                .toList();
        PagingResponse paging = PagingResponse.from(hospitalPaging);

        return HospitalListNearbyData.builder()
                .hospitalList(hospitalList)
                .paging(paging)
                .build();
    }

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
                        .totalGrade(0D)
                        .totalReviewCount(0L)
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
        Long totalBriefReviewCount = 0L;

        DoctorDetailsData.BriefReviewData briefReviewData = DoctorDetailsData.BriefReviewData.builder()
                .totalReviewCount(totalBriefReviewCount)
//                .kindness(calculateAverage(briefReviewSummary.getTotalKindnessSum(), totalBriefReviewCount))
//                .adhdUnderstanding(calculateAverage(briefReviewSummary.getTotalAdhdUnderstandingSum(), totalBriefReviewCount))
//                .enoughMedicalTime(calculateAverage(briefReviewSummary.getTotalEnoughMedicalTimeSum(), totalBriefReviewCount))
                .build();
        return DoctorDetailsData.builder()
                .name(doctor.getName())
                .totalGrade(0D)
                .totalReviewCount(0L)
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

    public HospitalReceiptReview getHospitalReceiptReview(String hospitalReceiptReviewId) {
        return hospitalReceiptReviewRepository.findById(hospitalReceiptReviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_HOSPITAL_RECEIPT_REVIEW));
    }

    public HospitalReceiptReviewListData getReceiptReviewList(String userId, String hospitalId, String doctorId, Pageable pageable) {
        Page<HospitalReceiptReviewDto> hospitalReceiptReviewPaging = hospitalReceiptReviewRepository
                .findAll(userId, hospitalId, doctorId, pageable);
        List<ReceiptReviewData> receiptReviewList = hospitalReceiptReviewPaging.getContent()
                .stream()
                .map(dto -> {
                    HospitalReceiptReview receiptReview = dto.getHospitalReceiptReview();
                    UserProfile writerProfile = dto.getUserProfile();
                    return ReceiptReviewData.builder()
                            .writerId(writerProfile.getUser().getId())
                            .name(writerProfile.getNickname())
                            .image(writerProfile.getProfileImage())
                            .totalGrade(null)
                            .createdAt(receiptReview.getCreatedAt())
                            .reviewImageList(receiptReview.getImages())
                            .content(receiptReview.getContent())
                            .helpCount(receiptReview.getHelpCount())
                            .isHelped(dto.isHelped())
                            .isMine(dto.isMine())
                            .build();
                }).toList();
        PagingResponse paging = PagingResponse.from(hospitalReceiptReviewPaging);

        return HospitalReceiptReviewListData.builder()
                .receiptReviewList(receiptReviewList)
                .paging(paging)
                .build();
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
    public void createReceiptReview(String userId, String hospitalId, String doctorId,
                                    HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData) {
        Doctor doctor = getDoctor(hospitalId, doctorId);
        validateDuplicatedHospitalReceiptReview(userId, doctorId);
        HospitalReceiptReview hospitalReceiptReview = HospitalReceiptReview.builder()
                .user(User.builder().id(userId).build())
                .doctor(doctor)
//                .kindness(hospitalReceiptReviewCreateData.getKindness())
//                .adhdUnderstanding(hospitalReceiptReviewCreateData.getAdhdUnderstanding())
//                .enoughMedicalTime(hospitalReceiptReviewCreateData.getEnoughMedicalTime())
                .content(hospitalReceiptReviewCreateData.getContent())
                .images(hospitalReceiptReviewCreateData.getImageList())
                .build();

        doctor.updateByCreatedReceiptReview();
        hospitalReceiptReviewRepository.save(hospitalReceiptReview);
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

    private void validateDuplicatedHospitalReceiptReview(String userId, String doctorId) {
        boolean existsHospitalReceiptReview = hospitalReceiptReviewRepository.findByUserIdAndDoctorId(userId, doctorId).isPresent();
        if (existsHospitalReceiptReview) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_HOSPITAL_RECEIPT_REVIEW);
        }
    }

    public void validateReceiptReviewer(HospitalReceiptReview hospitalReceiptReview, String userId) {
        String reviewerId = hospitalReceiptReview.getUser().getId();
        if (!Objects.equals(reviewerId, userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_HOSPITAL_RECEIPT_REVIEW);
        }
    }
}
