package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hospital extends BaseTimeEntity {

    @Id
    @GenericGenerator(name = "uuid-generator", type = com.project.foradhd.global.util.UUIDGenerator.class)
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "hospital_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Point location;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(length = 11)
    private String phone;
}
