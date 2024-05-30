package com.project.foradhd.domain.medicine.business.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.MedicineIngredient;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final RestTemplate restTemplate;  // RestTemplate은 Bean으로 등록 후 주입받도록 변경
    private final String API_URL = "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01";
    private final String SERVICE_KEY = "rzJVpYr3DAwYcKr+SyRZ5K0lIxsMeO5OdiaJrlGZ2O8C+B7oqEGRd96NskmVrzYItbIwhSD/Z2Y+ifVDTPlFkQ==";

    @Override
    public List<Medicine> fetchAndSaveMedicines() {
        String urlWithParameters = API_URL + "?serviceKey=" + SERVICE_KEY + "&numOfRows=100";  // 요청 파라미터 추가
        ResponseEntity<String> response = restTemplate.getForEntity(urlWithParameters, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode items = root.path("items").path("item");  // API 구조에 따라 경로 수정 필요

            List<Medicine> filteredMedicines = new ArrayList<>();

            if (items.isArray()) {
                for (JsonNode item : items) {
                    String ingredient = item.path("ingredient").asText();
                    if (MedicineIngredient.isRecognizedIngredient(ingredient)) {  // Enum을 통한 검증
                        Medicine medicine = Medicine.builder()
                                .itemName(item.path("itemName").asText())
                                .build();
                        medicineRepository.save(medicine);
                        filteredMedicines.add(medicine);
                    }
                }
            }
            return filteredMedicines;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON data", e);
        }
    }

    @Override
    public List<Medicine> findAllMedicines() {
        return medicineRepository.findAll();
    }
}