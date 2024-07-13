package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReview;
import com.project.foradhd.domain.hospital.persistence.repository.custom.HospitalReceiptReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospitalReceiptReviewRepository extends JpaRepository<HospitalReceiptReview, String>, HospitalReceiptReviewRepositoryCustom {

    @Query("""
        select hrr
        from HospitalReceiptReview hrr
        where hrr.id = :hospitalReceiptReviewId and hrr.deleted = false
    """)
    Optional<HospitalReceiptReview> findById(@Param("hospitalReceiptReviewId") String hospitalReceiptReviewId);

    @Query("""
        select hrr
        from HospitalReceiptReview hrr
        where hrr.user.id = :userId and hrr.receiptId = :receiptId and hrr.deleted = false
    """)
    Optional<HospitalReceiptReview> findByUserIdAndReceiptId(@Param("userId") String userId, @Param("receiptId") String receiptId);

    @Query("""
        select count(*)
        from HospitalReceiptReview hrr
        where hrr.hospital.id = :hospitalId and hrr.deleted = false
    """)
    int countByHospitalId(String hospitalId);

    @Query("""
        select count(*)
        from HospitalReceiptReview hrr
        where hrr.doctor.id = :doctorId and hrr.deleted = false
    """)
    int countByDoctorId(String doctorId);
}
