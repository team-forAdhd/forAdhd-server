package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineReviewRepository extends JpaRepository<MedicineReview, Long> {
    Page<MedicineReview> findByUserId(String userId, Pageable pageable);
}