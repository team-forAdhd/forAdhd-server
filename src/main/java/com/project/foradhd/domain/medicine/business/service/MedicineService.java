package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;

import java.io.IOException;
import java.util.List;

public interface MedicineService {
    List<Medicine> findAllMedicines();

    String fetchMedicineInfo(String itemName) throws IOException;
}