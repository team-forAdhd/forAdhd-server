package com.project.foradhd.domain.medicine.web.dto.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.List;

@Getter
public class MedicineReviewRequest {
    private String userId;
    private Long medicineId;
    private String content;
    private List<String> images;
    private float grade;

    public String getImagesAsJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(images);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert images list to JSON", e);
        }
    }
}
