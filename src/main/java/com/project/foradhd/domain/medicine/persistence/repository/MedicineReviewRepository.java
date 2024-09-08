package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MedicineReviewRepository extends JpaRepository<MedicineReview, Long> {
    Page<MedicineReview> findByUserId(String userId, Pageable pageable);
    Page<MedicineReview> findByMedicineId(Long medicineId, Pageable pageable);

    @Query("""
        SELECT r FROM MedicineReview r 
        JOIN FETCH r.user u 
        LEFT JOIN UserProfile up ON up.user.id = u.id 
        LEFT JOIN UserPrivacy uv ON uv.user.id = u.id 
        WHERE r.id = :reviewId
        """)
    Optional<MedicineReview> findByIdWithUserDetails(@Param("reviewId") Long reviewId);

    @Query("""
        SELECT r FROM MedicineReview r 
        JOIN FETCH r.user u 
        LEFT JOIN UserProfile up ON up.user.id = u.id 
        LEFT JOIN UserPrivacy uv ON uv.user.id = u.id 
        WHERE u.id = :userId
        """)
    Page<MedicineReview> findByUserIdWithDetails(@Param("userId") String userId, Pageable pageable);

    @Query("""
        SELECT r FROM MedicineReview r 
        JOIN FETCH r.user u 
        LEFT JOIN UserProfile up ON up.user.id = u.id 
        LEFT JOIN UserPrivacy uv ON uv.user.id = u.id 
        WHERE r.medicine.id = :medicineId
        """)
    Page<MedicineReview> findByMedicineIdWithDetails(@Param("medicineId") Long medicineId, Pageable pageable);
}