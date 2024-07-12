package com.project.foradhd.domain.medicine.web.dto.response;

import com.project.foradhd.domain.user.persistence.enums.Gender;
import lombok.*;
import java.util.List;

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
    private double averageGrade;
}
