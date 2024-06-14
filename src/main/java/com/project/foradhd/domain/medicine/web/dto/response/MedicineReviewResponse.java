package com.project.foradhd.domain.medicine.web.dto.response;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.audit.BaseTimeEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MedicineReviewResponse {

    private Long id;
    private String content;
    private List<String> images;
    private float grade;
    private int helpCount;

    private String nickname;
    private String profileImage;
    private String ageRange;
    private Gender gender;

    public MedicineReviewResponse(MedicineReview review, UserProfile userProfile, UserPrivacy userPrivacy) {
        this.id = review.getId();
        this.content = review.getContent();
        this.images = review.getImages();
        this.grade = review.getGrade();
        this.helpCount = review.getHelpCount();

        if (userProfile != null) {
            this.nickname = userProfile.getNickname();
            this.profileImage = userProfile.getProfileImage();
        }

        if (userPrivacy != null) {
            this.ageRange = userPrivacy.getAgeRange();
            this.gender = userPrivacy.getGender();
        }
    }
}
