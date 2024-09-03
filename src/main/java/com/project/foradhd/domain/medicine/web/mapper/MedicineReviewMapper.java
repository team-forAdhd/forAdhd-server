package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.repository.UserPrivacyRepository;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
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
    MedicineReviewResponse toResponseDto(MedicineReview review, @Context UserProfileRepository userProfileRepository,
                                         @Context UserPrivacyRepository userPrivacyRepository);

   /* @AfterMapping
    default void setUserDetails(@MappingTarget MedicineReviewResponse.MedicineReviewResponseBuilder responseBuilder,
                                MedicineReview review,
                                @Context UserProfileRepository userProfileRepository,
                                @Context UserPrivacyRepository userPrivacyRepository) {

        String userId = review.getUser().getId();

        // 최신 닉네임과 프로필 이미지 가져오기
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElse(null);
        if (userProfile != null) {
            responseBuilder.nickname(userProfile.getNickname())
                    .profileImage(userProfile.getProfileImage());
        } else {
            responseBuilder.nickname("Default Nickname")  // 기본 닉네임
                    .profileImage("default-profile-image-url");  // 기본 프로필 이미지 URL
        }

        // 최신 나이대와 성별 가져오기
        UserPrivacy userPrivacy = userPrivacyRepository.findByUserId(userId).orElse(null);
        if (userPrivacy != null) {
            responseBuilder.ageRange(userPrivacy.getAgeRange())
                    .gender(userPrivacy.getGender());
        }
    }*/

    @AfterMapping
    default void setMedicineId(@MappingTarget MedicineReviewResponse.MedicineReviewResponseBuilder responseBuilder, MedicineReview review) {
        responseBuilder.medicineId(review.getMedicine().getId());
    }
}