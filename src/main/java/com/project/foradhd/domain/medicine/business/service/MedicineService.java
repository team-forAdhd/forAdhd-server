package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;

import java.io.IOException;
import java.util.List;

public interface MedicineService {
    void saveMedicine(String itemname) throws IOException;
    String fetchMedicineInfo(String itemname) throws IOException;
    MedicineDto parseMedicine(String json);
    List<MedicineDto> getSortedMedicines(String sortOption);
    List<Medicine> searchByFormCodeNameAndShapeAndColor(String formCodeName, String shape, String color1);
    List<Medicine> searchByItemName(String itemName);
    MedicineDto getMedicineById(Long id);
    List<MedicineDto> getMedicinesByIngredientType(int ingredientType);
}