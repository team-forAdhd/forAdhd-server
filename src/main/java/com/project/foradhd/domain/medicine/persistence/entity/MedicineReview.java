package com.project.foradhd.domain.medicine.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "medicine_review")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineReview extends BaseTimeEntity {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(nullable = false)
    private Long medicineId;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column
    private String images; // JSON 형식으로 이미지 URL들 저장

    @Column(nullable = false)
    private float grade;

    @Column(nullable = false)
    private int helpCount;
}
