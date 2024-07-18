package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MedicineReviewService {
    MedicineReview createReview(MedicineReviewRequest request);
    MedicineReview updateReview(Long reviewId, MedicineReviewRequest request);
    void deleteReview(Long id);

    // 리뷰 목록을 정렬 옵션에 따라 조회
    Page<MedicineReview> findReviews(Pageable pageable);
    Page<MedicineReview> findReviewsByUserId(String userId, Pageable pageable);
    void incrementHelpCount(Long reviewId);
}