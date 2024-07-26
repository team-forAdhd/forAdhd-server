package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReviewLike;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewLikeRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService;
    private final MedicineReviewLikeRepository reviewLikeRepository;

    @Override
    @Transactional
    public MedicineReview createReview(MedicineReviewRequest request, String userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        }

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE));

        List<Long> coMedications = request.getCoMedications() != null ? request.getCoMedications() : Collections.emptyList();

        MedicineReview review = MedicineReview.builder()
                .user(user)
                .medicine(medicine)
                .coMedications(coMedications)
                .content(request.getContent())
                .images(request.getImages())
                .grade(request.getGrade())
                .helpCount(0)
                .build();

        return reviewRepository.save(review);
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
    public MedicineReview updateReview(Long reviewId, MedicineReviewRequest request) {
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

        return reviewRepository.save(updatedReview);
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
}
