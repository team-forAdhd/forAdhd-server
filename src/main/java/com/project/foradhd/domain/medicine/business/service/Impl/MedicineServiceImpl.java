package com.project.foradhd.domain.medicine.business.service.Impl;

import com.nimbusds.jose.shaded.gson.*;
import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import com.project.foradhd.domain.medicine.web.mapper.MedicineMapper;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    private static final String SERVICE_URL = "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01";
    private static final String SERVICE_KEY = "rzJVpYr3DAwYcKr%2BSyRZ5K0lIxsMeO5OdiaJrlGZ2O8C%2BB7oqEGRd96NskmVrzYItbIwhSD%2FZ2Y%2BifVDTPlFkQ%3D%3D";

    private static final Logger log = LoggerFactory.getLogger(MedicineServiceImpl.class);

    @Override
    @Transactional
    public void saveMedicine(String itemname) throws IOException {
        String json = fetchMedicineInfo(itemname);
        MedicineDto dto = parseMedicine(json);
        if (dto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_MEDICINE);
        }
        Medicine medicine = medicineMapper.toEntity(dto);
        medicineRepository.save(medicine);
    }

    public String fetchMedicineInfo(String itemname) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(SERVICE_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY);
        urlBuilder.append("&" + URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(itemname, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=10");
        urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=json");

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        connection.disconnect();

        return sb.toString();
    }

    public MedicineDto parseMedicine(String json) {
        try {
            Gson gson = new GsonBuilder().setLenient().create();
            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonResponse = jsonElement.getAsJsonObject();
                JsonArray items = jsonResponse.getAsJsonObject("body").getAsJsonArray("items");

                if (items != null && items.size() > 0) {
                    JsonObject item = items.get(0).getAsJsonObject();
                    return gson.fromJson(item, MedicineDto.class);
                }
            }
            return null;
        } catch (JsonSyntaxException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<MedicineDto> getSortedMedicines(String sortOption) {
        List<Medicine> medicines;
        switch (sortOption) {
            case "nameAsc":
                medicines = medicineRepository.findAllByOrderByItemNameAsc();
                break;
            case "ratingDesc":
                medicines = medicineRepository.findAllByOrderByRatingDesc();
                break;
            case "ratingAsc":
                medicines = medicineRepository.findAllByOrderByRatingAsc();
                break;
            case "ingredientAsc": // 성분 순 정렬
                List<Medicine> result = new ArrayList<>();
                result.addAll(medicineRepository.findByItemNameContainingOrderByItemNameAsc("메틸페니데이트"));
                result.addAll(medicineRepository.findByItemNameContainingOrderByItemNameAsc("아토목세틴"));
                result.addAll(medicineRepository.findByItemNameContainingOrderByItemNameAsc("클로니딘"));
                medicines = result;
                break;
            default:
                medicines = medicineRepository.findAll();
        }

        if (medicines.isEmpty()) {
            return Collections.emptyList();
        } else {
            return medicines.stream()
                    .map(medicineMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Medicine> searchByFormCodeNameAndShapeAndColor(String formCodeName, String shape, String color1) {
        List<Medicine> medicines = medicineRepository.findAllByFormCodeNameOrDrugShapeOrColorClass1(formCodeName, shape, color1);
        return medicines;
    }

    @Override
    public List<Medicine> searchByItemName(String itemName) {
        return medicineRepository.findByItemNameContaining(itemName);
    }

    @Override
    public MedicineDto getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE));
        return medicineMapper.toDto(medicine);
    }
}
