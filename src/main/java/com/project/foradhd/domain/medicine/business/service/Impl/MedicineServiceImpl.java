package com.project.foradhd.domain.medicine.business.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.Color;
import com.project.foradhd.domain.medicine.persistence.enums.DosageForm;
import com.project.foradhd.domain.medicine.persistence.enums.Shape;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineFilteringRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final RestTemplate restTemplate;
    private final String SERVICE_URL = "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01";
    private final String SERVICE_KEY = "rzJVpYr3DAwYcKr%2BSyRZ5K0lIxsMeO5OdiaJrlGZ2O8C%2BB7oqEGRd96NskmVrzYItbIwhSD%2FZ2Y%2BifVDTPlFkQ%3D%3D\n"; // 실제 서비스 키를 사용하세요

    @Autowired
    private MedicineRepository medicineRepository;

    public String getMedicineInfo(String itemName) {
        String url = UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("itemName", itemName)
                .queryParam("numOfRows", "10")
                .queryParam("pageNo", "1")
                .queryParam("type", "json")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch data: " + response.getStatusCode());
        }
    }

    @Override
    public List<JsonNode> getFilteredMedicineInfo(MedicineFilteringRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("itemName", request.getItemName())
                .queryParam("type", "json")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to fetch data: " + response.getStatusCode());
        }

        List<JsonNode> filteredItems = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode items = root.path("body").path("items").path("item");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    String currentForm = item.path("formCodeName").asText("");
                    String currentShape = item.path("drugShape").asText("");
                    String currentColor = item.path("colorClass1").asText("");
                    if (currentForm.equalsIgnoreCase(request.getForm()) &&
                            currentShape.equalsIgnoreCase(request.getShape()) &&
                            currentColor.equalsIgnoreCase(request.getColor())) {
                        filteredItems.add(item);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON response: " + e.getMessage(), e);
        }
        return filteredItems;
    }

    @Override
    public Medicine findById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Medicine> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public void deleteById(Long id) {
        medicineRepository.deleteById(id);
    }
    public Page<Medicine> searchMedicines(DosageForm dosageForm, Shape shape, Color color, Pageable pageable) {
        return medicineRepository.findByDosageFormAndShapeAndColor(dosageForm, shape, color, pageable);
    }
}
