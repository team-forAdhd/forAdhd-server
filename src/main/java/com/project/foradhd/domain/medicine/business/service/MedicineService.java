package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;

import java.util.List;

public interface MedicineService {
    List<Medicine> fetchAndSaveMedicines();
    List<Medicine> findAllMedicines();
}