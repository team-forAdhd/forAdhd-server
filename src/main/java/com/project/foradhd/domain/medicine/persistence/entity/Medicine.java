package com.project.foradhd.domain.medicine.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "medicines")
public class Medicine {

    @jakarta.persistence.Id
    @Column(name = "medicine_id")
    private Long id;
    private String itemSeq;
    private String itemName;
    private String entpSeq;
    private String entpName;
    private String chart;
    private String itemImage;
    private String drugShape;
    private String colorClass1;
    private String colorClass2;
    private String classNo;
    private String className;
    private String etcOtcName;
    private String itemEngName;
}
