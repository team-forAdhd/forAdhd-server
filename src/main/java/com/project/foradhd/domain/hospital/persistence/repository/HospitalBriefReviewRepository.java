package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBriefReviewSummary;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalBriefReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HospitalBriefReviewRepository extends JpaRepository<HospitalBriefReview, String> {

    @Query("""
        select new com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBriefReviewSummary(
            count(hbr), sum(hbr.kindness), sum(hbr.adhdUnderstanding), sum(hbr.enoughMedicalTime))
        from HospitalBriefReview hbr
        where hbr.doctor.id = :doctorId and hbr.deleted = false
    """)
    HospitalBriefReviewSummary getSummaryByDoctorId(@Param("doctorId") String doctorId);
}
