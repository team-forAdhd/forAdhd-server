package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    @Query("""
        select d
        from Doctor d
        where d.id = :doctorId and d.deleted = false
    """)
    Optional<Doctor> findById(@Param("doctorId") String doctorId);

    @Query("""
        select d
        from Doctor d
        inner join Hospital h on h.id = d.hospital.id and h.id = :hospitalId and h.deleted = false
        where d.id = :doctorId and d.deleted = false
    """)
    Optional<Doctor> findByIdAndHospitalId(@Param("doctorId") String doctorId, @Param("hospitalId") String hospitalId);

    @Query("""
        select d
        from Doctor d
        where d.hospital.id = :hospitalId and d.deleted = false
        order by d.name asc
    """)
    List<Doctor> findAllByHospitalId(String hospitalId);
}
