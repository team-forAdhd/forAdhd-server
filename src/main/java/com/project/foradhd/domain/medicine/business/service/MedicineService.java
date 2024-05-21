package com.project.foradhd.domain.medicine.business.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.Color;
import com.project.foradhd.domain.medicine.persistence.enums.DosageForm;
import com.project.foradhd.domain.medicine.persistence.enums.Shape;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineFilteringRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicineService {
    Medicine findById(Long id);
    Page<Medicine> findAll(Pageable pageable);
    List<Medicine> findAll();
    Medicine save(Medicine medicine);
    void deleteById(Long id);
    public Page<Medicine> searchMedicines(DosageForm dosageForm, Shape shape, Color color, Pageable pageable);
    String getMedicineInfo(String itemName);
    List<JsonNode> getFilteredMedicineInfo(MedicineFilteringRequest request);
}