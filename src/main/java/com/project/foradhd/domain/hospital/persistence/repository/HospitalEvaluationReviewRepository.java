package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospitalEvaluationReviewRepository extends JpaRepository<HospitalEvaluationReview, String> {

    @Query("""
        select her
        from HospitalEvaluationReview her
        join fetch her.hospitalEvaluationAnswerList heal
        join fetch heal.hospitalEvaluationQuestion
        where her.id = :hospitalEvaluationReviewId
    """)
    Optional<HospitalEvaluationReview> findByIdFetchAll(@Param("hospitalEvaluationReviewId") String hospitalEvaluationReviewId);

    @Query("""
        select her
        from HospitalEvaluationReview her
        join fetch her.hospitalEvaluationAnswerList heal
        where her.id = :hospitalEvaluationReviewId
    """)
    Optional<HospitalEvaluationReview> findByIdFetch(@Param("hospitalEvaluationReviewId") String hospitalEvaluationReviewId);

    @Query("""
        select her
        from HospitalEvaluationReview her
        where her.user.id = :userId and her.hospital.id = :hospitalId
    """)
    Optional<HospitalEvaluationReview> findByUserIdAndHospitalId(@Param("userId") String userId, @Param("hospitalId") String hospitalId);

    @Query("""
        select count(*)
        from HospitalEvaluationReview her
        where her.hospital.id = :hospitalId
    """)
    int countByHospitalId(@Param("hospitalId") String hospitalId);
}
