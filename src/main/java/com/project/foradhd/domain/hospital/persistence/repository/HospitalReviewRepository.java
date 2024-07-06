package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.dto.out.MyHospitalReviewDto;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewFilter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface HospitalReviewRepository {

    List<MyHospitalReviewDto> findMyHospitalReviewList(String userId, HospitalReviewFilter filter, Pageable pageable);

    long countMyHospitalReviewList(String userId, HospitalReviewFilter filter);
}
