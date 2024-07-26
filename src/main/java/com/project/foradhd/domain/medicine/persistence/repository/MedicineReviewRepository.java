package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicineReviewRepository extends JpaRepository<MedicineReview, Long> {
    Page<MedicineReviewResponse> findByUserId(String userId, Pageable pageable);
}