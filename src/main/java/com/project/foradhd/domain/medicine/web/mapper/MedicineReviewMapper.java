package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicineReviewMapper {

    @Mapping(target = "medicine.id", source = "medicineId")
    @Mapping(target = "ageRange", ignore = true)
    @Mapping(target = "gender", ignore = true)
    MedicineReview toEntity(MedicineReviewRequest request, @Context User user, @Context Medicine medicine);

    @Mapping(target = "averageGrade", expression = "java(review.getMedicine().calculateAverageGrade())")
    @Mapping(target = "images", source = "review.images")
    @Mapping(target = "coMedications", source = "review.coMedications")
    MedicineReviewResponse toResponseDto(MedicineReview review, @Context UserService userService);

    @AfterMapping
    default void setUserProfileDetails(@MappingTarget MedicineReviewResponse.MedicineReviewResponseBuilder responseBuilder,
                                       MedicineReview review,
                                       @Context UserService userService) {
        // 리뷰의 유저 ID를 사용해 실시간으로 유저 정보를 가져옴
        UserProfile userProfile = userService.getUserProfile(review.getUser().getId());
        UserPrivacy userPrivacy = userService.getUserPrivacy(review.getUser().getId());

        if (userProfile != null) {
            responseBuilder.nickname(userProfile.getNickname());
            responseBuilder.profileImage(userProfile.getProfileImage());
        }
        if (userPrivacy != null) {
            responseBuilder.ageRange(userPrivacy.getAgeRange());
            responseBuilder.gender(userPrivacy.getGender());
        }
    }

    @AfterMapping
    default void setMedicineId(@MappingTarget MedicineReviewResponse.MedicineReviewResponseBuilder responseBuilder, MedicineReview review) {
        responseBuilder.medicineId(review.getMedicine().getId());
    }
}
