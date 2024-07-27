package com.project.foradhd.domain.medicine.web.controller;

import com.project.foradhd.domain.medicine.business.service.Impl.MedicineServiceImpl;
import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineSearchResponse;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineSortedResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    @Autowired
    private final MedicineService medicineService;
    @Autowired
    private final MedicineMapper medicineMapper;

   // 의약품 데이터를 가져와 저장하는 API
    @GetMapping("/fetch-and-save")
    public ResponseEntity<Object> fetchAndSaveMedicine(@RequestParam String itemname) {
        try {
            medicineService.saveMedicine(itemname);
            return ResponseEntity.ok(Map.of("message", "Medicine data successfully fetched and saved."));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to fetch or save medicine data", "details", e.getMessage()));
        }
    }

    // 의약품 목록을 정렬 옵션에 따라 조회하는 API
    @GetMapping("/sorted")
    public ResponseEntity<MedicineSortedResponse> getSortedMedicines(
            @RequestParam(defaultValue = "nameAsc") String sortOption) {
        try {
            List<MedicineDto> medicines = medicineService.getSortedMedicines(sortOption);
            MedicineSortedResponse response = MedicineSortedResponse.builder()
                    .kind("종류")
                    .medicineList(medicines)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 의약품을 검색하는 API
    @GetMapping("/search")
    public ResponseEntity<List<MedicineSearchResponse>> searchMedicines(
            @RequestParam(required = false) String shape,
            @RequestParam(required = false) String color1,
            @RequestParam(required = false) String formCodeName,
            @RequestParam(required = false) String itemName) {

        List<Medicine> medicines;

        if (itemName != null) {
            medicines = medicineService.searchByItemName(itemName);
        } else if (shape != null || color1 != null || formCodeName != null) {
            medicines = medicineService.searchByFormCodeNameAndShapeAndColor(formCodeName, shape, color1);
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<MedicineSearchResponse> response = medicineMapper.toResponseDtoList(medicines);
        return ResponseEntity.ok(response);
    }

    // 개별 약 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable Long id) {
        MedicineDto medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(medicine);
    }
}