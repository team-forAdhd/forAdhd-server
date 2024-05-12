package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBriefReviewSummary;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalBriefReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospitalBriefReviewRepository extends JpaRepository<HospitalBriefReview, String> {

    @Query("""
        select hbr
        from HospitalBriefReview hbr
        where hbr.id = :hospitalBriefReviewId and hbr.deleted = false
    """)
    Optional<HospitalBriefReview> findById(@Param("hospitalBriefReviewId") String hospitalBriefReviewId);

    @Query("""
        select new com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBriefReviewSummary(
            count(hbr), sum(hbr.kindness), sum(hbr.adhdUnderstanding), sum(hbr.enoughMedicalTime))
        from HospitalBriefReview hbr
        where hbr.doctor.id = :doctorId and hbr.deleted = false
    """)
    HospitalBriefReviewSummary getSummaryByDoctorId(@Param("doctorId") String doctorId);

    @Modifying
    @Query("""
        update HospitalBriefReview hbr
        set hbr.deleted = true
        where hbr.id = :hospitalBriefReviewId
    """)
    void deleteSoftly(@Param("hospitalBriefReviewId") String hospitalBriefReviewId);
}
