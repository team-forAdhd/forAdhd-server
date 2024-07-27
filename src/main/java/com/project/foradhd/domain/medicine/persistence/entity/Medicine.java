package com.project.foradhd.domain.medicine.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemSeq; //품목일련번호
    private String itemName; //품목명
    private String entpSeq; //업체일련번호
    private String entpName; //업체명
    private String chart; //성상
    private String itemImage; //큰제품이미지
    private String drugShape;  //의약품모양
    private String colorClass1; //색상앞
    private String colorClass2; //색상뒤
    private String classNo; //분류번호
    private String className; //분류명
    private String formCodeName; //제형코드명
    private String itemEngName; //제품영문명
    private double rating; // 별점
    private boolean isFavorite; // 즐겨찾기 여부

    @OneToMany(mappedBy = "medicine")
    private List<MedicineReview> reviews;

    public double calculateAverageGrade() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(MedicineReview::getGrade)
                .average()
                .orElse(0.0);
    }

}
