package com.project.foradhd.domain.medicine.fixtures;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.user.persistence.entity.User;

import java.util.Arrays;
import java.util.List;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;

public class MedicineFixtures {

    public static Medicine.MedicineBuilder toMedicine() {
        return Medicine.builder()
                .id(1L)
                .itemName("Aspirin");
    }

    public static MedicineReview.MedicineReviewBuilder toMedicineReview() {
        User user = toUser().build();
        Medicine medicine = toMedicine().build();
        return MedicineReview.builder()
                .user(user)
                .medicine(medicine)
                .content("약 리뷰 내용")
                .grade(4.5)
                .images(List.of("images/img1.png", "images/img2.png"))
                .coMedications(Arrays.asList(1L, 2L));
    }
}
