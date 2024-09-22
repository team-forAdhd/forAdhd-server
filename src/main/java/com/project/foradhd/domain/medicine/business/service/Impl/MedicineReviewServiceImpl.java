package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReviewLike;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewLikeRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MedicineReviewServiceImpl implements MedicineReviewService {
    private final MedicineReviewRepository reviewRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineReviewLikeRepository reviewLikeRepository;
    private final UserService userService;

    @Override
    @Transactional
    public MedicineReview createReview(MedicineReviewRequest request, String userId) {
        User user = userService.getUser(userId);

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE));

        MedicineReview review = MedicineReview.builder()
                .medicine(medicine)
                .user(user)
                .content(request.getContent())
                .grade(request.getGrade())
                .images(request.getImages())
                .coMedications(request.getCoMedications())
                .build();

        MedicineReview savedReview = reviewRepository.save(review);

        // 약의 평균 별점을 업데이트
        updateMedicineRating(medicine);

        return savedReview; // DTO 변환 없이 엔티티를 반환
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

        // 리뷰 작성자와 요청한 유저가 같은지 확인
        if (!existingReview.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_MEDICINE_REVIEW);
        }

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

        return savedReview; // DTO 변환 없이 엔티티를 반환
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String userId) {
        // 리뷰가 존재하는지 확인
        MedicineReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE_REVIEW));

        // 리뷰 작성자와 요청한 유저가 동일한지 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_MEDICINE_REVIEW);
        }

        // 리뷰 삭제
        reviewRepository.deleteById(reviewId);

        // 약물의 평균 별점을 업데이트
        Medicine medicine = review.getMedicine();
        updateMedicineRating(medicine);
    }

    @Override
    public Page<MedicineReview> findReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }


    @Override
    public Page<MedicineReview> findReviewsByUserId(String userId, Pageable pageable, SortOption sortOption) {
        Sort sort = getSortByOption(sortOption);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return reviewRepository.findByUserIdWithDetails(userId, sortedPageable); // 엔티티를 반환
    }

    @Override
    public Page<MedicineReview> findReviewsByMedicineId(Long medicineId, Pageable pageable, SortOption sortOption) {
        Sort sort = getSortByOption(sortOption);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return reviewRepository.findByMedicineIdWithDetails(medicineId, sortedPageable); // 엔티티를 반환
    }

    private void updateMedicineRating(Medicine medicine) {
        double averageGrade = medicine.calculateAverageGrade();
        Medicine updatedMedicine = medicine.toBuilder()
                .rating(averageGrade)
                .build();
        medicineRepository.save(updatedMedicine);
    }

    private Sort getSortByOption(SortOption sortOption) {
        switch (sortOption) {
            case NEWEST_FIRST:
                return Sort.by(Sort.Direction.DESC, "createdAt");
            case OLDEST_FIRST:
                return Sort.by(Sort.Direction.ASC, "createdAt");
            case HIGHEST_GRADE:
                return Sort.by(Sort.Direction.DESC, "grade");
            case LOWEST_GRADE:
                return Sort.by(Sort.Direction.ASC, "grade");
            default:
                return Sort.by(Sort.Direction.DESC, "createdAt");
        }
    }
}
