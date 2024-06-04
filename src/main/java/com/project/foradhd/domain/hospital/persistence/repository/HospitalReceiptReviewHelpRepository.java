package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReviewHelp;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReviewHelp.HospitalReceiptReviewHelpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HospitalReceiptReviewHelpRepository extends JpaRepository<HospitalReceiptReviewHelp, HospitalReceiptReviewHelpId> {

    @Query("""
        select count(hrrh)
        from HospitalReceiptReviewHelp hrrh
        where hrrh.id.hospitalReceiptReview.id = :hospitalReceiptReviewId and hrrh.deleted = false
    """)
    Integer countHelp(@Param("hospitalReceiptReviewId") String hospitalReceiptReviewId);
}
