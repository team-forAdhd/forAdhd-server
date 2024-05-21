package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;

public interface MedicineReviewService {
    MedicineReview createReview(MedicineReviewRequest request);
    void incrementHelpCount(Long reviewId);
    MedicineReview updateReview(Long reviewId, MedicineReviewRequest request);
}


