package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.project.foradhd.domain.hospital.persistence.repository.custom.HospitalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, String>, HospitalRepositoryCustom {

    @Query("""
        select h
        from Hospital h
        where h.id = :hospitalId and h.deleted = false
    """)
    Optional<Hospital> findById(@Param("hospitalId") String hospitalId);
}
