package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
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

@Mapper(componentModel = "spring")
public interface MedicineReviewMapper {
    MedicineReview toEntity(MedicineReviewRequest request, @Context User user, @Context Medicine medicine);

    @Mapping(target = "nickname", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "ageRange", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "averageGrade", expression = "java(review.getMedicine().calculateAverageGrade())")
    MedicineReviewResponse toResponseDto(MedicineReview review);

    @AfterMapping
    default void setUserDetails(@MappingTarget MedicineReviewResponse.MedicineReviewResponseBuilder responseBuilder, MedicineReview review,
                                @Context UserProfileRepository userProfileRepository, @Context UserPrivacyRepository userPrivacyRepository) {
        String userId = review.getUser().getId();
        responseBuilder.nickname(userProfileRepository.findByUserId(userId).map(UserProfile::getNickname).orElse(null))
                .profileImage(userProfileRepository.findByUserId(userId).map(UserProfile::getProfileImage).orElse(null))
                .ageRange(userPrivacyRepository.findByUserId(userId).map(UserPrivacy::getAgeRange).orElse(null))
                .gender(userPrivacyRepository.findByUserId(userId).map(UserPrivacy::getGender).orElse(Gender.UNKNOWN));
    }
}
