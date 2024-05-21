package com.project.foradhd.domain.medicine.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "medicine_bookmark")
public class MedicineBookmark extends BaseEntity {

    @jakarta.persistence.Id
    @Column(name = "medicine_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastModifiedAt;

    @ColumnDefault("0")
    private Boolean deleted;
}
