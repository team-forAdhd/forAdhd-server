package com.project.foradhd.domain.hospital.fixtures;

import com.project.foradhd.domain.hospital.persistence.entity.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.List;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;

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

    public static HospitalEvaluationQuestion.HospitalEvaluationQuestionBuilder toHospitalEvaluationQuestion() {
        return HospitalEvaluationQuestion.builder()
                .id(1L)
                .question("해당 병원은 뇌파검사를 실시하고 있다.")
                .seq(1);
    }

    public static HospitalEvaluationReview.HospitalEvaluationReviewBuilder toHospitalEvaluationReview() {
        return HospitalEvaluationReview.builder()
                .id("hospitalEvaluationReviewId")
                .hospital(toHospital().build())
                .user(toUser().build());
    }

    public static HospitalEvaluationAnswer.HospitalEvaluationAnswerBuilder toHospitalEvaluationAnswer() {
        return HospitalEvaluationAnswer.builder()
                .id(1L)
                .hospitalEvaluationReview(toHospitalEvaluationReview().build())
                .hospitalEvaluationQuestion(toHospitalEvaluationQuestion().build())
                .answer(true);
    }

    public static HospitalReceiptReview.HospitalReceiptReviewBuilder toHospitalReceiptReview() {
        return HospitalReceiptReview.builder()
                .id("hospitalReceiptReviewId")
                .user(toUser().build())
                .hospital(toHospital().build())
                .content("영수증 리뷰내용리뷰내용리뷰내용리뷰내용리뷰내용")
                .images(List.of("/images/image.png", "/images/image.jpg", "/images/image.jpeg"))
                .medicalExpense(15000L)
                .helpCount(5);
    }
}
