package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.repository.*;
import com.project.foradhd.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.project.foradhd.domain.hospital.fixtures.HospitalFixtures.toDoctor;
import static com.project.foradhd.domain.hospital.fixtures.HospitalFixtures.toHospital;
import static com.project.foradhd.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("HospitalService 테스트")
@ExtendWith(MockitoExtension.class)
class HospitalServiceTest {

    @InjectMocks
    HospitalService hospitalService;

    @Mock
    HospitalRepository hospitalRepository;

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    HospitalBookmarkRepository hospitalBookmarkRepository;

    @Mock
    HospitalReceiptReviewRepository hospitalReceiptReviewRepository;

    @Mock
    HospitalReceiptReviewHelpRepository hospitalReceiptReviewHelpRepository;

    @Mock
    HospitalEvaluationReviewRepository hospitalEvaluationReviewRepository;

    @Mock
    HospitalEvaluationQuestionRepository hospitalEvaluationQuestionRepository;

    @Mock
    HospitalReviewRepository hospitalReviewRepository;
    
    @DisplayName("병원 상세 조회 테스트")
    @Test
    void get_hospital_details() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        Doctor doctor = toDoctor().build();
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(toHospital().build()));
        given(doctorRepository.findAllByHospitalIdOrderByName(hospitalId)).willReturn(List.of(doctor));
        given(hospitalBookmarkRepository.findById(userId, hospitalId)).willReturn(Optional.empty());
        given(hospitalEvaluationReviewRepository.findByUserIdAndHospitalId(userId, hospitalId)).willReturn(Optional.empty());

        //when
        HospitalDetailsData hospitalDetails = hospitalService.getHospitalDetails(userId, hospitalId);

        //then
        then(hospitalRepository).should(times(1)).findById(hospitalId);
        then(doctorRepository).should(times(1)).findAllByHospitalIdOrderByName(hospitalId);
        then(hospitalBookmarkRepository).should(times(1)).findById(userId, hospitalId);
        then(hospitalEvaluationReviewRepository).should(times(1)).findByUserIdAndHospitalId(userId, hospitalId);
        assertThat(hospitalDetails.getIsBookmarked()).isFalse();
        assertThat(hospitalDetails.getIsEvaluationReviewed()).isFalse();
    }

    @DisplayName("ID로 병원 조회 테스트 - 실패")
    @Test
    void get_hospital_by_id_test_fail() {
        //given
        String hospitalId = "hospitalId";
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> hospitalService.getHospital(hospitalId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_HOSPITAL);
        then(hospitalRepository).should(times(1)).findById(hospitalId);
    }

    @DisplayName("ID로 의사 조회 테스트 - 실패")
    @Test
    void get_doctor_by_id_test_fail() {
        //given
        String doctorId = "doctorId";
        given(doctorRepository.findById(doctorId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> hospitalService.getDoctor(doctorId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_DOCTOR);
        then(doctorRepository).should(times(1)).findById(doctorId);
    }

    @DisplayName("병원 ID 및 의사 ID로 의사 조회 테스트 - 실패")
    @Test
    void get_doctor_by_hospital_id_and_doctor_id_test_fail() {
        //given
        String hospitalId = "hospitalId";
        String doctorId = "doctorId";
        given(doctorRepository.findByIdAndHospitalId(doctorId, hospitalId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> hospitalService.getDoctor(hospitalId, doctorId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_DOCTOR);
        then(doctorRepository).should(times(1)).findByIdAndHospitalId(doctorId, hospitalId);
    }

    @DisplayName("ID로 병원 영수증 리뷰 조회 테스트 - 실패")
    @Test
    void get_hospital_receipt_review_test_fail() {
        //given
        String hospitalReceiptReviewId = "hospitalReceiptReviewId";
        given(hospitalReceiptReviewRepository.findById(hospitalReceiptReviewId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> hospitalService.getHospitalReceiptReview(hospitalReceiptReviewId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_HOSPITAL_RECEIPT_REVIEW);
        then(hospitalReceiptReviewRepository).should(times(1)).findById(hospitalReceiptReviewId);
    }

    @DisplayName("ID로 병원 평가 리뷰 조회 테스트 - 실패")
    @Test
    void get_hospital_evaluation_review_test_fail() {
        //given
        String hospitalEvaluationReviewId = "hospitalEvaluationReviewId";
        given(hospitalEvaluationReviewRepository.findById(hospitalEvaluationReviewId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> hospitalService.getHospitalEvaluationReview(hospitalEvaluationReviewId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_HOSPITAL_EVALUATION_REVIEW);
        then(hospitalEvaluationReviewRepository).should(times(1)).findById(hospitalEvaluationReviewId);
    }
}
