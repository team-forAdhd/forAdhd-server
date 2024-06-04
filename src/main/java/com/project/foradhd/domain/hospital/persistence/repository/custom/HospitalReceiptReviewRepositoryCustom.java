package com.project.foradhd.domain.hospital.persistence.repository.custom;

import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalReceiptReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalReceiptReviewRepositoryCustom {

    Page<HospitalReceiptReviewDto> findAll(String userId, String hospitalId, String doctorId, Pageable pageable);
}
