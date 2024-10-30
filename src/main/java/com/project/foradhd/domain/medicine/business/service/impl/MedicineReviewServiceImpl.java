package com.project.foradhd.domain.medicine.business.service.impl;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReviewLikeFilter;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MedicineReviewServiceImpl implements MedicineReviewService {

    private final UserService userService;
    private final MedicineReviewRepository medicineReviewRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineReviewLikeRepository medicineReviewLikeRepository;

    @Override
    @Transactional
    public MedicineReview createReview(MedicineReviewRequest request, String userId) {
        User user = userService.getUser(userId);

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE));

        MedicineReview medicineReview = MedicineReview.builder()
                .medicine(medicine)
                .user(user)
                .content(request.getContent())
                .grade(request.getGrade())
                .images(request.getImages())
                .coMedications(request.getCoMedications())
                .build();

        MedicineReview savedReview = medicineReviewRepository.save(medicineReview);

        // 약의 평균 별점을 업데이트
        updateMedicineRating(medicine);

        return savedReview; // DTO 변환 없이 엔티티를 반환
    }

    @Override
    @Transactional
    public void toggleHelpCount(Long reviewId, String userId) {
        MedicineReview medicineReview = medicineReviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE_REVIEW));
        User user = userService.getUser(userId);

        if (medicineReviewLikeRepository.existsByUserIdAndMedicineReviewId(userId, reviewId)) {
            medicineReviewLikeRepository.deleteByUserIdAndMedicineReviewId(userId, reviewId);
            medicineReview.decrementHelpCount();
        } else {
            MedicineReviewLikeFilter newLike = MedicineReviewLikeFilter.builder()
                    .user(user)
                    .medicineReview(medicineReview)
                    .build();
            medicineReviewLikeRepository.save(newLike);
            medicineReview.incrementHelpCount();
        }
    }

    @Override
    @Transactional
    public MedicineReview updateReview(Long reviewId, MedicineReviewRequest request, String userId) {
        MedicineReview existingReview = medicineReviewRepository.findById(reviewId)
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

        MedicineReview savedReview = medicineReviewRepository.save(updatedReview);

        // 약의 평균 별점을 업데이트
        updateMedicineRating(medicine);

        return savedReview; // DTO 변환 없이 엔티티를 반환
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String userId) {
        // 리뷰가 존재하는지 확인
        MedicineReview medicineReview = medicineReviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE_REVIEW));

        // 리뷰 작성자와 요청한 유저가 동일한지 확인
        if (!medicineReview.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_MEDICINE_REVIEW);
        }

        // 리뷰 삭제
        medicineReviewRepository.deleteById(reviewId);

        // 약물의 평균 별점을 업데이트
        Medicine medicine = medicineReview.getMedicine();
        updateMedicineRating(medicine);
    }

    @Override
    public Page<MedicineReview> findReviews(Pageable pageable) {
        return medicineReviewRepository.findAll(pageable);
    }


    @Override
    public Page<MedicineReview> findReviewsByUserId(String userId, Pageable pageable, SortOption sortOption) {
        Sort sort = getSortByOption(sortOption);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return medicineReviewRepository.findByUserIdWithDetails(userId, sortedPageable); // 엔티티를 반환
    }

    @Override
    public Page<MedicineReview> findReviewsByMedicineId(Long medicineId, Pageable pageable, SortOption sortOption) {
        Sort sort = getSortByOption(sortOption);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return medicineReviewRepository.findByMedicineIdWithDetails(medicineId, sortedPageable); // 엔티티를 반환
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
