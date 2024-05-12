package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalBookmark;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalBookmark.HospitalBookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospitalBookmarkRepository extends JpaRepository<HospitalBookmark, HospitalBookmarkId> {

    @Query("""
        select hb
        from HospitalBookmark hb
        where hb.id.user.id = :userId 
            and hb.id.hospital.id = :hospitalId
    """)
    Optional<HospitalBookmark> findById(@Param("userId") String userId, @Param("hospitalId") String hospitalId);
}
