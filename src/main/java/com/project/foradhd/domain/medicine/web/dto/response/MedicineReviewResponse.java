package com.project.foradhd.domain.medicine.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MedicineReviewResponse {
    private Long id;
    private Long medicineId;
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
    @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
    private LocalDateTime lastModifiedAt;
}
