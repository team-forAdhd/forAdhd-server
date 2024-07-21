package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
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

@Component
@Mapper(componentModel = "spring")
public abstract class MedicineReviewMapper {

    @Autowired
    protected UserProfileRepository userProfileRepository;

    @Autowired
    protected UserPrivacyRepository userPrivacyRepository;

    @Mapping(target = "nickname", source = "user.id", qualifiedByName = "getNickname")
    @Mapping(target = "profileImage", source = "user.id", qualifiedByName = "getProfileImage")
    @Mapping(target = "ageRange", source = "user.id", qualifiedByName = "getAgeRange")
    @Mapping(target = "gender", source = "user.id", qualifiedByName = "getGender")
    @Mapping(target = "averageGrade", expression = "java(review.getMedicine().calculateAverageGrade())")
    public abstract MedicineReviewResponse toResponseDto(MedicineReview review);

    @Named("getNickname")
    protected String getNickname(String userId) {
        return userProfileRepository.findByUserId(userId).map(UserProfile::getNickname).orElse(null);
    }

    @Named("getProfileImage")
    protected String getProfileImage(String userId) {
        return userProfileRepository.findByUserId(userId).map(UserProfile::getProfileImage).orElse(null);
    }

    @Named("getAgeRange")
    protected String getAgeRange(String userId) {
        return userPrivacyRepository.findByUserId(userId).map(UserPrivacy::getAgeRange).orElse(null);
    }

    @Named("getGender")
    protected Gender getGender(String userId) {
        return userPrivacyRepository.findByUserId(userId).map(UserPrivacy::getGender).orElse(Gender.UNKNOWN);
    }
}
