package com.project.foradhd.domain.medicine.web.dto.response;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.audit.BaseTimeEntity;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineReviewResponse {
    private Long id;
    private String content;
    private List<String> images;
    private float grade;
    private int helpCount;
    private List<Long> coMedications;
    private String nickname;
    private String profileImage;
    private String ageRange;
    private Gender gender;
}
