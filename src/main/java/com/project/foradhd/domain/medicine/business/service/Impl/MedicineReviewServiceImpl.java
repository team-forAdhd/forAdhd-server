package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

import static com.project.foradhd.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MedicineReviewServiceImpl implements MedicineReviewService {
    private final MedicineReviewRepository reviewRepository;
    private final MedicineRepository medicineRepository;
    private final UserService userService;

    @Override
    @Transactional
    public MedicineReview createReview(MedicineReviewRequest request) {
        User user = userService.getUser(request.getUserId());
        if (user == null) {
            throw new BusinessException(NOT_FOUND_USER);
        }

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_MEDICINE));

        List<Long> coMedications = Collections.emptyList(); // 빈 리스트로 초기화
        if (request.getCoMedications() != null && !request.getCoMedications().isEmpty()) {
            coMedications = request.getCoMedications();
        }

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
    public void incrementHelpCount(Long reviewId) {
        MedicineReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_MEDICINE_REVIEW));
        int newHelpCount = review.getHelpCount() + 1;

        MedicineReview updatedReview = MedicineReview.builder()
                .id(review.getId())
                .user(review.getUser())
                .medicine(review.getMedicine())
                .coMedications(review.getCoMedications())
                .content(review.getContent())
                .images(review.getImages())
                .grade(review.getGrade())
                .helpCount(newHelpCount)
                .build();

        reviewRepository.save(updatedReview);
    }

    @Override
    @Transactional
    public MedicineReview updateReview(Long reviewId, MedicineReviewRequest request) {
        MedicineReview existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_MEDICINE));

        List<Long> coMedications = request.getCoMedications();

        MedicineReview updatedReview = MedicineReview.builder()
                .id(existingReview.getId())
                .user(existingReview.getUser())
                .medicine(medicine)
                .coMedications(coMedications)
                .content(request.getContent())
                .images(request.getImages())
                .grade(request.getGrade())
                .helpCount(existingReview.getHelpCount())
                .build();

        return reviewRepository.save(updatedReview);
    }

    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new BusinessException(NOT_FOUND_MEDICINE_REVIEW);
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
