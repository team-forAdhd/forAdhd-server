package com.project.foradhd.domain.hospital.persistence.repository.custom;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalListBookmarkSearchCond;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBookmarkDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalNearbyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalRepositoryCustom {

    Page<HospitalNearbyDto> findAllNearby(String userId, HospitalListNearbySearchCond searchCond, Pageable pageable);

    Page<HospitalBookmarkDto> findAllBookmark(String userId, HospitalListBookmarkSearchCond searchCond, Pageable pageable);
}
