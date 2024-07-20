package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalListBookmarkData {

    private List<HospitalBookmarkData> hospitalList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class HospitalBookmarkData {

        private Hospital hospital;
    }
}
