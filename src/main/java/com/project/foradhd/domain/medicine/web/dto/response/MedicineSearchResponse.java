package com.project.foradhd.domain.medicine.web.dto.response;

import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MedicineSearchResponse {
    private List<MedicineSearchListResponse> data;
    private PagingResponse paging;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicineSearchListResponse {
        private String itemImage;  // 약 이미지 URL
        private String itemName;   // 약 이름
        private String itemEngName; // 약 영문명
        private String entpName;    // 제조사 이름
        private Long medicineId;  // 약 ID
    }
}