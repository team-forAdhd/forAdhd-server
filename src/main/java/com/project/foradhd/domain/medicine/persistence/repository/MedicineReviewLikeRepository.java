package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineReviewLikeRepository extends JpaRepository<MedicineReviewLike, Long> {
    boolean existsByUserIdAndReviewId(String userId, Long reviewId);
    void deleteByUserIdAndReviewId(String userId, Long reviewId);
}
