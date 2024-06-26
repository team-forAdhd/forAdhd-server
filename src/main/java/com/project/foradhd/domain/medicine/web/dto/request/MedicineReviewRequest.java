package com.project.foradhd.domain.medicine.web.dto.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineReviewRequest {
    private String userId; // 사용자 ID
    private Long medicineId; // 리뷰 대상 의약품 ID
    private List<Medicine> coMedications; // 함께 복용하는 의약품 ID 목록
    private String content; // 리뷰 내용
    private List<String> images; // 이미지 URL 목록
    private float grade; // 평점
}
