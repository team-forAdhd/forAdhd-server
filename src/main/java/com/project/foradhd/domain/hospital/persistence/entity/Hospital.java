package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hospital extends BaseTimeEntity {

    @Id
    @GenericGenerator(name = "uuid-generator", type = com.project.foradhd.global.util.UUIDGenerator.class)
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "hospital_id", columnDefinition = "varchar(32)")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Point location;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(length = 11)
    private String phone;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Doctor> doctorList;
}
