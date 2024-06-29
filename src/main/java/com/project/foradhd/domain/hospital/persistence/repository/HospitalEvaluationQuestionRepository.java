package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HospitalEvaluationQuestionRepository extends JpaRepository<HospitalEvaluationQuestion, Long> {

    @Query("select heq from HospitalEvaluationQuestion heq where heq.deleted = false order by heq.seq")
    List<HospitalEvaluationQuestion> findAll();
}
