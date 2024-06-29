package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicineReviewMapper {
    @Mapping(source = "user.userProfile.nickname", target = "nickname")
    @Mapping(source = "user.userProfile.profileImage", target = "profileImage")
    @Mapping(source = "user.userPrivacy.ageRange", target = "ageRange")
    @Mapping(source = "user.userPrivacy.gender", target = "gender")
    MedicineReviewResponse toResponseDto(MedicineReview review);
}
