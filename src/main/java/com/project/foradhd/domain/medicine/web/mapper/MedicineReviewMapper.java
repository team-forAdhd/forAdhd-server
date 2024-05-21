package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineReviewMapper {
    MedicineReviewResponse toResponseDto(MedicineReview medicineReview);
}
