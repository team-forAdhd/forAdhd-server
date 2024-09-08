package com.project.foradhd.domain.medicine.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Builder
@Entity
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="medicine_details")
public class MedicineDetails extends BaseTimeEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_details_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Medicine medicine;

    private String content;
}
