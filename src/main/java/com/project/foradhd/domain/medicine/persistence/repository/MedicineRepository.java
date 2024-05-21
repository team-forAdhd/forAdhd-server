package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.Color;
import com.project.foradhd.domain.medicine.persistence.enums.DosageForm;
import com.project.foradhd.domain.medicine.persistence.enums.Shape;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Page<Medicine> findByDosageFormAndShapeAndColor(DosageForm dosageForm, Shape shape, Color color, Pageable pageable);
}
