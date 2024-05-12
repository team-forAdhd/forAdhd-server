package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, String> {

    @Query("""
        select distinct h 
        from Hospital h 
        left join fetch h.doctorList d 
        where h.id = :hospitalId
        order by d.name asc
    """)
    Optional<Hospital> findByIdFetch(@Param("hospitalId") String hospitalId);
}
