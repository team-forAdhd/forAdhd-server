package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    @Query("""
        select d
        from Doctor d
        inner join Hospital h on h.id = d.hospital.id and h.id = :hospitalId and h.deleted = false
        where d.id = :doctorId and d.deleted = false
    """)
    Optional<Doctor> findByIdAndHospitalId(@Param("doctorId") String doctorId, @Param("hospitalId") String hospitalId);
}
