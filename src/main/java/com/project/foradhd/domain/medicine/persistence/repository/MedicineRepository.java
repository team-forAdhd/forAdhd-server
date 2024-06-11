package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findAllByOrderByItemNameAsc();
    List<Medicine> findAllByOrderByRatingDesc();
    List<Medicine> findAllByOrderByRatingAsc();
    List<Medicine> findByItemNameContainingOrderByItemNameAsc(String ingredient);
    List<Medicine> findAllByFormCodeNameOrDrugShapeOrColorClass1(String formCodeName, String drugShape, String color1);
    List<Medicine> findByItemNameContaining(String itemName);
}