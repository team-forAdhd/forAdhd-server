package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineSearchHistoryRepository extends JpaRepository<MedicineSearchHistory, Long> {
    List<MedicineSearchHistory> findTop10ByUserIdOrderBySearchedAtDesc(String userId);
}