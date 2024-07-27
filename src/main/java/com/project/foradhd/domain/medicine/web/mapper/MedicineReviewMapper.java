package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.repository.UserPrivacyRepository;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserService.class, MedicineService.class})
public interface MedicineReviewMapper {
    @Mapping(target = "medicine.id", source = "medicineId")
    @Mapping(target = "nickname", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "ageRange", ignore = true)
    @Mapping(target = "gender", ignore = true)
    MedicineReview toEntity(MedicineReviewRequest request, @Context User user, @Context Medicine medicine);

    @Mapping(target = "nickname", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "ageRange", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "averageGrade", expression = "java(review.getMedicine().calculateAverageGrade())")
    @Mapping(target = "images", source = "review.images")
    @Mapping(target = "coMedications", source = "review.coMedications")
    MedicineReviewResponse toResponseDto(MedicineReview review);

    @AfterMapping
    default void setUserDetails(@MappingTarget MedicineReviewResponse.MedicineReviewResponseBuilder responseBuilder, MedicineReview review) {
        if (review.getUser() != null) {
            responseBuilder.nickname(review.getNickname())
                    .profileImage(review.getProfileImage())
                    .ageRange(review.getAgeRange())
                    .gender(review.getGender());
        }
    }

    @AfterMapping
    default void setMedicineId(@MappingTarget MedicineReviewResponse.MedicineReviewResponseBuilder responseBuilder, MedicineReview review) {
        responseBuilder.medicineId(review.getMedicine().getId());
    }
}
