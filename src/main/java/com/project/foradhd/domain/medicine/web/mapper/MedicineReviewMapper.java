package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.user.business.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface MedicineReviewMapper {

    MedicineReviewResponse toResponseDto(MedicineReview medicineReview);
}
