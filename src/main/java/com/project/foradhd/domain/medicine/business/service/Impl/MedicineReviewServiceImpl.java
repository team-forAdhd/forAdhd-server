package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.foradhd.global.exception.ErrorCode.NOT_FOUND_USER;

@RequiredArgsConstructor
@Service
public class MedicineReviewServiceImpl implements MedicineReviewService {
    private final MedicineReviewRepository reviewRepository;
    private final UserService userService;
    
    @Override
    public MedicineReview createReview(MedicineReviewRequest request) {
        // UserService를 사용하여 사용자가 유효한지 확인
        User user = userService.getUser(request.getUserId());
        if (user == null) {
            throw new BusinessException(NOT_FOUND_USER);
        }

        MedicineReview review = MedicineReview.builder()
                .user(userService.getUser(request.getUserId()))
                .medicineId(request.getMedicineId())
                .content(request.getContent())
                .images(request.getImagesAsJson())
                .grade(request.getGrade())
                .helpCount(0)
                .build();
        return reviewRepository.save(review);
    }

    @Override
    public void incrementHelpCount(Long reviewId) {
        MedicineReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.setHelpCount(review.getHelpCount() + 1);
        reviewRepository.save(review);
    }

    @Override
    public MedicineReview updateReview(Long reviewId, MedicineReviewRequest request) {
        MedicineReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setContent(request.getContent());
        review.setImages(request.getImagesAsJson());
        review.setGrade(request.getGrade());
        return reviewRepository.save(review);
    }
}
