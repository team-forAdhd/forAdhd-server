package com.project.foradhd.domain.medicine.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.Color;
import com.project.foradhd.domain.medicine.persistence.enums.DosageForm;
import com.project.foradhd.domain.medicine.persistence.enums.Shape;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineBookmarkRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineFilteringRequest;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineFilteringResponse;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineMapper;
import com.project.foradhd.global.AuthUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MedicineBookmarkRepository bookmarkRepository;

    @Autowired
    private MedicineMapper medicineMapper;

    private String userId;

    @GetMapping
    public ResponseEntity<Page<MedicineResponse>> getAllMedicines(
            @RequestParam(required = false) String sortOption,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthUserId String userId
    ) {
        Pageable sortedPageable = handleSorting(sortOption, pageable);

        if ("favorites".equals(sortOption)) {
            Page<Medicine> favorites = bookmarkRepository.existsByUserIdAndMedicineId(userId, sortedPageable);
            return ResponseEntity.ok(favorites.map(medicineMapper::medicineToMedicineResponseDto));
        } else {
            Page<Medicine> page = medicineService.findAll(sortedPageable);
            return ResponseEntity.ok(page.map(medicineMapper::medicineToMedicineResponseDto));
        }
    }

    private Pageable handleSorting(String sortOption, Pageable pageable) {
        switch (sortOption) {
            case "nameAsc":
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
            case "ratingHigh":
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "totalGrade"));
            case "ratingLow":
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("totalGrade"));
            case "favorites":
                // 즐겨찾기 정렬은 별도의 처리가 필요할 수 있으며, 정렬 로직을 여기서 적용하지 않습니다.
                return pageable;
            default:
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getMedicine(@PathVariable Long id) {
        Medicine medicine = medicineService.findById(id);
        if (medicine == null) {
            return ResponseEntity.notFound().build();
        }
        MedicineResponse responseDto = medicineMapper.medicineToMedicineResponseDto(medicine);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<MedicineResponse> createMedicine(@RequestBody MedicineRequest requestDto) {
        Medicine medicine = medicineMapper.medicineRequestDtoToMedicine(requestDto);
        Medicine savedMedicine = medicineService.save(medicine);
        MedicineResponse responseDto = medicineMapper.medicineToMedicineResponseDto(savedMedicine);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Medicine>> searchMedicines(
            @RequestParam DosageForm dosageForm,
            @RequestParam Shape shape,
            @RequestParam Color color,
            Pageable pageable) {
        Page<Medicine> medicines = medicineService.searchMedicines(dosageForm, shape, color, pageable);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/info")
    public ResponseEntity<String> getMedicineInfo(@RequestParam String itemName) {
        String result = medicineService.getMedicineInfo(itemName);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/filtered-info")
    public ResponseEntity<List<MedicineFilteringResponse>> getFilteredMedicineInfo(@RequestBody MedicineFilteringRequest request) {
        List<JsonNode> jsonNodes = medicineService.getFilteredMedicineInfo(request);
        List<MedicineFilteringResponse> result = jsonNodes.stream()
                .map(medicineMapper::toMedicineFilteringResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
