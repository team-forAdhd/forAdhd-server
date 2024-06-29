package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalEvaluationAnswerRepository extends JpaRepository<HospitalEvaluationAnswer, Long> {

    @Query("""
        select hea
        from HospitalEvaluationAnswer hea
        join fetch hea.hospitalEvaluationQuestion
        join fetch hea.hospitalEvaluationReview
        where hea.hospitalEvaluationReview.id = :hospitalEvaluationReviewId
    """)
    List<HospitalEvaluationAnswer> findAllByUserIdAndReviewId(@Param("hospitalEvaluationReviewId") String hospitalEvaluationReviewId);
}
