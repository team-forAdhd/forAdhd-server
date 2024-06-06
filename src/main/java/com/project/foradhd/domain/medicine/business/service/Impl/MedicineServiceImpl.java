package com.project.foradhd.domain.medicine.business.service.Impl;

import com.nimbusds.jose.shaded.gson.*;
import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private static final String SERVICE_URL = "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01";
    private static final String SERVICE_KEY = "rzJVpYr3DAwYcKr+SyRZ5K0lIxsMeO5OdiaJrlGZ2O8C+B7oqEGRd96NskmVrzYItbIwhSD/Z2Y+ifVDTPlFkQ==";

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public String fetchMedicineInfo(String itemName) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(SERVICE_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(SERVICE_KEY, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(itemName, "UTF-8")); // 품목명 지정
        urlBuilder.append("&" + URLEncoder.encode("entp_name", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); // 업체명
        urlBuilder.append("&" + URLEncoder.encode("item_seq", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); // 품목일련번호
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

    private List<MedicineDto> parseMedicines(String json) {
        Gson gson = new Gson();
        MedicineResponse response = gson.fromJson(json, MedicineResponse.class);
        if (response != null && response.getBody() != null && response.getBody().getItems() != null) {
            return response.getBody().getItems();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Medicine> findAllMedicines() {
        return medicineRepository.findAll();
    }
}