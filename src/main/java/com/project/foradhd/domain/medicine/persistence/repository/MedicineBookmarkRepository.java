package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineBookmarkRepository extends JpaRepository<MedicineBookmark, Long> {
    boolean existsByUserIdAndMedicineId(String userId, Long medicineId);
    void deleteByUserIdAndMedicineId(String userId, Long medicineId);
    Page<MedicineBookmark> findByUserIdAndDeletedIsFalse(String userId, Pageable pageable);
}
