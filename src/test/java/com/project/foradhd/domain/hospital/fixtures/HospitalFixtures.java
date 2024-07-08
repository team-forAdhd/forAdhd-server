package com.project.foradhd.domain.hospital.fixtures;

import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class HospitalFixtures {

    public static Hospital.HospitalBuilder toHospital() {
        GeometryFactory geometryFactory = new GeometryFactory();
        double latitude = 37.5326;
        double longitude = 126.9901;
        Coordinate coordinate = new Coordinate(longitude, latitude);

        return Hospital.builder()
                .id("hospitalId")
                .name("용산구정신건강의학과의원")
                .location(geometryFactory.createPoint(coordinate))
                .address("서울특별시 용산구 한강대로 259")
                .phone("027109856")
                .totalReceiptReviewCount(5)
                .totalEvaluationReviewCount(7)
                .deleted(false);
    }

    public static Doctor.DoctorBuilder toDoctor() {
        return Doctor.builder()
                .id("doctorId")
                .hospital(toHospital().build())
                .name("김코코")
                .image("/default/doctor/image")
                .profile("학력\n" +
                        "서울대학교 의과대학 졸업\n" +
                        "서울대학교 대학원 의학석사\n" +
                        "서울대학교 대학원 의학박사")
                .totalReceiptReviewCount(5)
                .deleted(false);
    }
}
