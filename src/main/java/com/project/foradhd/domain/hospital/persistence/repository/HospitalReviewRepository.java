package com.project.foradhd.domain.hospital.persistence.repository;

import com.project.foradhd.domain.hospital.persistence.dto.out.MyHospitalReviewDto;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewFilter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HospitalReviewRepository {

    List<MyHospitalReviewDto> findMyHospitalReviewList(String userId, HospitalReviewFilter filter, Pageable pageable);

    long countMyHospitalReviewList(String userId, HospitalReviewFilter filter);
}
