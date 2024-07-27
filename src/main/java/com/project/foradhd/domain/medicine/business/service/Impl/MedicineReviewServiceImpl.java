package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReviewLike;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewLikeRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineReviewMapper;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.repository.UserPrivacyRepository;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MedicineReviewServiceImpl implements MedicineReviewService {
    private final MedicineReviewRepository reviewRepository;
    private final MedicineRepository medicineRepository;
    @Qualifier("medicineReviewMapper")
    private final MedicineReviewMapper reviewMapper;
    private final MedicineReviewLikeRepository reviewLikeRepository;
    private final UserService userService;
    private final UserProfileRepository userProfileRepository;
    private final UserPrivacyRepository userPrivacyRepository;

    @Override
    @Transactional
    public MedicineReview createReview(MedicineReviewRequest request, String userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        }

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE));

        // 사용자 상세 정보 조회
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_PROFILE));
        UserPrivacy userPrivacy = userPrivacyRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        MedicineReview review = MedicineReview.builder()
                .medicine(medicine)
                .user(user)
                .content(request.getContent())
                .grade(request.getGrade())
                .images(request.getImages())
                .coMedications(request.getCoMedications())
                .nickname(userProfile.getNickname())
                .profileImage(userProfile.getProfileImage())
                .ageRange(userPrivacy.getAgeRange())
                .gender(userPrivacy.getGender())
                .build();

        MedicineReview savedReview = reviewRepository.save(review);

        // 약의 평균 별점을 업데이트
        updateMedicineRating(medicine);

        return savedReview;
    }


    @Override
    @Transactional
    public void toggleHelpCount(Long reviewId, String userId) {
        MedicineReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE_REVIEW));
        User user = userService.getUser(userId);

        if (reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            reviewLikeRepository.deleteByUserIdAndReviewId(userId, reviewId);
            review = review.toBuilder().helpCount(review.getHelpCount() - 1).build();
        } else {
            MedicineReviewLike newLike = MedicineReviewLike.builder()
                    .user(user)
                    .review(review)
                    .build();
            reviewLikeRepository.save(newLike);
            review = review.toBuilder().helpCount(review.getHelpCount() + 1).build();
        }

        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public MedicineReview updateReview(Long reviewId, MedicineReviewRequest request, String userId) {
        MedicineReview existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE_REVIEW));

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE));

        MedicineReview updatedReview = existingReview.toBuilder()
                .medicine(medicine)
                .coMedications(request.getCoMedications())
                .content(request.getContent())
                .images(request.getImages())
                .grade(request.getGrade())
                .build();

        MedicineReview savedReview = reviewRepository.save(updatedReview);

        // 약의 평균 별점을 업데이트
        updateMedicineRating(medicine);

        return savedReview;
    }


    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_MEDICINE_REVIEW);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public Page<MedicineReview> findReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Page<MedicineReview> findReviewsByUserId(String userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable);
    }

    private void updateMedicineRating(Medicine medicine) {
        double averageGrade = medicine.calculateAverageGrade();
        Medicine updatedMedicine = medicine.toBuilder()
                .rating(averageGrade)
                .build();
        medicineRepository.save(updatedMedicine);
    }

    @Override
    public Page<MedicineReview> findReviewsByMedicineId(Long medicineId, Pageable pageable) {
        return reviewRepository.findByMedicineId(medicineId, pageable);
    }
}
