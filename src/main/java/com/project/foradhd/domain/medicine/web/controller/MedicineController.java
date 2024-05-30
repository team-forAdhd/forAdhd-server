package com.project.foradhd.domain.medicine.web.controller;

import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;
    private final MedicineMapper medicineMapper;

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
        List<Medicine> medicines = medicineService.findAllMedicines();
        List<MedicineResponse> responseDTOs = medicines.stream()
                .map(medicineMapper::medicineToMedicineResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping("/fetch")
    public ResponseEntity<Void> fetchAndSaveMedicines() {
        try {
            List<Medicine> medicines = medicineService.fetchAndSaveMedicines();
            if (medicines.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}