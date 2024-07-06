package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.domain.hospital.web.enums.HospitalReviewType;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyHospitalReviewListData {

    private List<MyHospitalReviewData> hospitalReviewList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class MyHospitalReviewData {

        private String hospitalReviewId;
        private String hospitalId;
        private String hospitalName;
        private HospitalReviewType reviewType;
        private Long createdAt;
        private String content;
        private List<String> imageList;
    }
}
