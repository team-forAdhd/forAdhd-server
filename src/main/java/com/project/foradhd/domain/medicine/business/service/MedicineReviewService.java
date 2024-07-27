package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MedicineReviewService {
    MedicineReview createReview(MedicineReviewRequest request, String userId);
    void toggleHelpCount(Long reviewId, String userId);
    MedicineReview updateReview(Long reviewId, MedicineReviewRequest request, String userId);
    void deleteReview(Long id);
    Page<MedicineReview> findReviews(Pageable pageable);
    Page<MedicineReview> findReviewsByUserId(String userId, Pageable pageable);
    Page<MedicineReview> findReviewsByMedicineId(Long medicineId, Pageable pageable);
}

