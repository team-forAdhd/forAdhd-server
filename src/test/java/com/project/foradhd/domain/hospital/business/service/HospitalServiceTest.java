package com.project.foradhd.domain.hospital.business.service;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalEvaluationReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalEvaluationReviewCreateData.HospitalEvaluationAnswerCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalEvaluationReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.business.service.impl.HospitalServiceImpl;
import com.project.foradhd.domain.hospital.persistence.entity.*;
import com.project.foradhd.domain.hospital.persistence.repository.*;
import com.project.foradhd.global.exception.BusinessException;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.project.foradhd.domain.hospital.fixtures.HospitalFixtures.*;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static com.project.foradhd.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("HospitalService 테스트")
@ExtendWith(MockitoExtension.class)
class HospitalServiceTest {

    @InjectMocks
    HospitalServiceImpl hospitalService;

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

    @Captor
    ArgumentCaptor<HospitalEvaluationReview> hospitalEvaluationReviewArgumentCaptor;

    @Captor
    ArgumentCaptor<HospitalBookmark> hospitalBookmarkArgumentCaptor;

    @Captor
    ArgumentCaptor<HospitalReceiptReview> hospitalReceiptReviewArgumentCaptor;

    @Captor
    ArgumentCaptor<HospitalReceiptReviewHelp> hospitalReceiptReviewHelpArgumentCaptor;
    
    @DisplayName("병원 상세 조회 테스트")
    @Test
    void get_hospital_details() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        Double latitude = 37.4867657;
        Double longitude = 127.1031943;
        Doctor doctor = toDoctor().build();
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(toHospital().build()));
        given(doctorRepository.findAllByHospitalIdOrderByName(hospitalId)).willReturn(List.of(doctor));
        given(hospitalBookmarkRepository.findById(userId, hospitalId)).willReturn(Optional.empty());
        given(hospitalEvaluationReviewRepository.findByUserIdAndHospitalId(userId, hospitalId)).willReturn(Optional.empty());

        //when
        HospitalDetailsData hospitalDetails = hospitalService.getHospitalDetails(userId, hospitalId, latitude, longitude);

        //then
        then(hospitalRepository).should(times(1)).findById(hospitalId);
        then(doctorRepository).should(times(1)).findAllByHospitalIdOrderByName(hospitalId);
        then(hospitalBookmarkRepository).should(times(1)).findById(userId, hospitalId);
        then(hospitalEvaluationReviewRepository).should(times(1)).findByUserIdAndHospitalId(userId, hospitalId);
        assertThat(hospitalDetails.getDistance()).isNotNull();
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

    @DisplayName("포에이리본 병원 평가 리뷰 작성 테스트")
    @Test
    void create_evaluation_review_test() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        Hospital hospital = toHospital().build();
        int totalEvaluationReviewCount = 10;
        HospitalEvaluationQuestion hospitalEvaluationQuestion1 = toHospitalEvaluationQuestion().id(1L).build();
        HospitalEvaluationQuestion hospitalEvaluationQuestion2 = toHospitalEvaluationQuestion().id(2L).build();
        HospitalEvaluationQuestion hospitalEvaluationQuestion3 = toHospitalEvaluationQuestion().id(3L).build();
        HospitalEvaluationReviewCreateData hospitalEvaluationReviewCreateData = HospitalEvaluationReviewCreateData.builder()
                .hospitalEvaluationAnswerList(
                        List.of(HospitalEvaluationAnswerCreateData.builder()
                                        .hospitalEvaluationQuestionId(1L)
                                        .answer(true)
                                        .build(),
                                HospitalEvaluationAnswerCreateData.builder()
                                        .hospitalEvaluationQuestionId(2L)
                                        .answer(false)
                                        .build(),
                                HospitalEvaluationAnswerCreateData.builder()
                                        .hospitalEvaluationQuestionId(3L)
                                        .answer(true)
                                        .build()))
                .build();
        given(hospitalEvaluationReviewRepository.findByUserIdAndHospitalId(userId, hospitalId)).willReturn(Optional.empty());
        given(hospitalEvaluationQuestionRepository.findAll()).willReturn(List.of(
                hospitalEvaluationQuestion1,
                hospitalEvaluationQuestion2,
                hospitalEvaluationQuestion3));
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));
        given(hospitalEvaluationReviewRepository.countByHospitalId(hospitalId)).willReturn(totalEvaluationReviewCount);

        //when
        hospitalService.createEvaluationReview(userId, hospitalId, hospitalEvaluationReviewCreateData);

        //then
        then(hospitalEvaluationReviewRepository).should(times(1)).save(hospitalEvaluationReviewArgumentCaptor.capture());
        HospitalEvaluationReview savedHospitalEvaluationReview = hospitalEvaluationReviewArgumentCaptor.getValue();
        List<HospitalEvaluationAnswer> savedHospitalEvaluationAnswerList = savedHospitalEvaluationReview.getHospitalEvaluationAnswerList();

        assertThat(savedHospitalEvaluationReview.getHospital().getId()).isEqualTo(hospitalId);
        assertThat(savedHospitalEvaluationReview.getUser().getId()).isEqualTo(userId);
        assertThat(savedHospitalEvaluationAnswerList).hasSize(3);
        assertThat(savedHospitalEvaluationAnswerList)
                .extracting("hospitalEvaluationReview", "hospitalEvaluationQuestion", "answer")
                .containsAll(List.of(
                        Tuple.tuple(savedHospitalEvaluationReview, hospitalEvaluationQuestion1, true),
                        Tuple.tuple(savedHospitalEvaluationReview, hospitalEvaluationQuestion2, false),
                        Tuple.tuple(savedHospitalEvaluationReview, hospitalEvaluationQuestion3, true)));
        assertThat(hospital.getTotalEvaluationReviewCount()).isEqualTo(totalEvaluationReviewCount);
    }

    @DisplayName("포에이리본 병원 평가 리뷰 수정 테스트")
    @Test
    void update_evaluation_review_test() {
        //given
        String userId = "userId";
        String hospitalEvaluationReviewId = "hospitalEvaluationReviewId";
        HospitalEvaluationReview hospitalEvaluationReview = toHospitalEvaluationReview().id(hospitalEvaluationReviewId).user(toUser().id(userId).build()).build();
        HospitalEvaluationAnswer hospitalEvaluationAnswer1 = toHospitalEvaluationAnswer().hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(1L).build()).answer(false).build();
        HospitalEvaluationAnswer hospitalEvaluationAnswer2 = toHospitalEvaluationAnswer().hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(2L).build()).answer(false).build();
        HospitalEvaluationAnswer hospitalEvaluationAnswer3 = toHospitalEvaluationAnswer().hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(3L).build()).answer(false).build();
        hospitalEvaluationReview.updateHospitalEvaluationAnswerList(List.of(hospitalEvaluationAnswer1, hospitalEvaluationAnswer2, hospitalEvaluationAnswer3));
        HospitalEvaluationReviewUpdateData hospitalEvaluationReviewUpdateData = HospitalEvaluationReviewUpdateData.builder()
                .hospitalEvaluationAnswerList(List.of(
                        HospitalEvaluationReviewUpdateData.HospitalEvaluationAnswerUpdateData.builder()
                                .hospitalEvaluationQuestionId(1L)
                                .answer(true)
                                .build(),
                        HospitalEvaluationReviewUpdateData.HospitalEvaluationAnswerUpdateData.builder()
                                .hospitalEvaluationQuestionId(2L)
                                .answer(true)
                                .build(),
                        HospitalEvaluationReviewUpdateData.HospitalEvaluationAnswerUpdateData.builder()
                                .hospitalEvaluationQuestionId(3L)
                                .answer(true)
                                .build()))
                .build();
        given(hospitalEvaluationReviewRepository.findByIdFetch(hospitalEvaluationReviewId)).willReturn(Optional.of(hospitalEvaluationReview));

        //when
        hospitalService.updateEvaluationReview(userId, hospitalEvaluationReviewId, hospitalEvaluationReviewUpdateData);

        //then
        assertAll(
                () -> assertThat(hospitalEvaluationAnswer1.getAnswer()).isTrue(),
                () -> assertThat(hospitalEvaluationAnswer2.getAnswer()).isTrue(),
                () -> assertThat(hospitalEvaluationAnswer3.getAnswer()).isTrue());
    }

    @DisplayName("포에이리본 병원 평가 리뷰 삭제 테스트")
    @Test
    void delete_evaluation_review_test() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        String hospitalEvaluationReviewId = "hospitalEvaluationReviewId";
        int totalEvaluationReviewCount = 10;
        Hospital hospital = toHospital().build();
        HospitalEvaluationReview hospitalEvaluationReview = toHospitalEvaluationReview().id(hospitalEvaluationReviewId).hospital(hospital).user(toUser().id(userId).build()).build();
        HospitalEvaluationAnswer hospitalEvaluationAnswer1 = toHospitalEvaluationAnswer().hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(1L).build()).answer(false).build();
        HospitalEvaluationAnswer hospitalEvaluationAnswer2 = toHospitalEvaluationAnswer().hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(2L).build()).answer(false).build();
        HospitalEvaluationAnswer hospitalEvaluationAnswer3 = toHospitalEvaluationAnswer().hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(3L).build()).answer(false).build();
        hospitalEvaluationReview.updateHospitalEvaluationAnswerList(List.of(hospitalEvaluationAnswer1, hospitalEvaluationAnswer2, hospitalEvaluationAnswer3));
        given(hospitalEvaluationReviewRepository.findByIdFetch(hospitalEvaluationReviewId)).willReturn(Optional.of(hospitalEvaluationReview));
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));
        given(hospitalEvaluationReviewRepository.countByHospitalId(hospitalId)).willReturn(totalEvaluationReviewCount);

        //when
        hospitalService.deleteEvaluationReview(userId, hospitalEvaluationReviewId);

        //then
        then(hospitalEvaluationReviewRepository).should(times(1)).delete(hospitalEvaluationReview);
        assertThat(hospital.getTotalEvaluationReviewCount()).isEqualTo(totalEvaluationReviewCount);
    }

    @DisplayName("병원 북마크 테스트")
    @Test
    void save_hospital_bookmark_test() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        boolean bookmark = true;
        Hospital hospital = toHospital().id(hospitalId).build();
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));

        //when
        hospitalService.saveHospitalBookmark(userId, hospitalId, bookmark);

        //then
        then(hospitalBookmarkRepository).should(times(1)).save(hospitalBookmarkArgumentCaptor.capture());
        HospitalBookmark hospitalBookmark = hospitalBookmarkArgumentCaptor.getValue();
        assertThat(hospitalBookmark.getId().getHospital().getId()).isEqualTo(hospitalId);
        assertThat(hospitalBookmark.getId().getUser().getId()).isEqualTo(userId);
        assertThat(hospitalBookmark.getDeleted()).isFalse();
    }

    @DisplayName("병원 영수증 리뷰 작성 테스트")
    @Test
    void create_hospital_receipt_review_test() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        int totalReceiptReviewCount = 10;
        Hospital hospital = toHospital().id(hospitalId).build();
        HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData = HospitalReceiptReviewCreateData.builder()
                .content("영수증 리뷰내용리뷰내용리뷰내용리뷰내용리뷰내용")
                .imageList(List.of("/images/image1.png", "/images/image1.jpg", "/images/image1.jpeg"))
                .medicalExpense(15000L)
                .build();
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));
        given(hospitalReceiptReviewRepository.countByHospitalId(hospitalId)).willReturn(totalReceiptReviewCount);

        //when
        hospitalService.createReceiptReview(userId, hospitalId, hospitalReceiptReviewCreateData);

        //then
        then(hospitalReceiptReviewRepository).should(times(1)).save(hospitalReceiptReviewArgumentCaptor.capture());
        HospitalReceiptReview hospitalReceiptReview = hospitalReceiptReviewArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(hospitalReceiptReview.getUser().getId()).isEqualTo(userId),
                () -> assertThat(hospitalReceiptReview.getHospital().getId()).isEqualTo(hospitalId),
                () -> assertThat(hospitalReceiptReview.getContent()).isEqualTo(hospitalReceiptReviewCreateData.getContent()),
                () -> assertThat(hospitalReceiptReview.getImages()).containsAll(hospitalReceiptReviewCreateData.getImageList()),
                () -> assertThat(hospitalReceiptReview.getMedicalExpense()).isEqualTo(hospitalReceiptReviewCreateData.getMedicalExpense())
        );
        assertThat(hospital.getTotalReceiptReviewCount()).isEqualTo(totalReceiptReviewCount);
    }

    @DisplayName("의사 영수증 리뷰 작성 테스트")
    @Test
    void create_doctor_receipt_review_test() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        String doctorId = "doctorId";
        int totalHospitalReceiptReviewCount = 10;
        int totalDoctorReceiptReviewCount = 10;
        Hospital hospital = toHospital().id(hospitalId).build();
        Doctor doctor = toDoctor().id(doctorId).build();
        HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData = HospitalReceiptReviewCreateData.builder()
                .content("영수증 리뷰내용리뷰내용리뷰내용리뷰내용리뷰내용")
                .imageList(List.of("/images/image.png", "/images/image.jpg", "/images/image.jpeg"))
                .medicalExpense(15000L)
                .build();
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));
        given(doctorRepository.findByIdAndHospitalId(doctorId, hospitalId)).willReturn(Optional.of(doctor));
        given(hospitalReceiptReviewRepository.countByHospitalId(hospitalId)).willReturn(totalHospitalReceiptReviewCount);
        given(hospitalReceiptReviewRepository.countByDoctorId(doctorId)).willReturn(totalDoctorReceiptReviewCount);

        //when
        hospitalService.createReceiptReview(userId, hospitalId, doctorId, hospitalReceiptReviewCreateData);

        //then
        then(hospitalReceiptReviewRepository).should(times(1)).save(hospitalReceiptReviewArgumentCaptor.capture());
        HospitalReceiptReview hospitalReceiptReview = hospitalReceiptReviewArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(hospitalReceiptReview.getUser().getId()).isEqualTo(userId),
                () -> assertThat(hospitalReceiptReview.getHospital().getId()).isEqualTo(hospitalId),
                () -> assertThat(hospitalReceiptReview.getDoctor().getId()).isEqualTo(doctorId),
                () -> assertThat(hospitalReceiptReview.getContent()).isEqualTo(hospitalReceiptReviewCreateData.getContent()),
                () -> assertThat(hospitalReceiptReview.getImages()).containsAll(hospitalReceiptReviewCreateData.getImageList()),
                () -> assertThat(hospitalReceiptReview.getMedicalExpense()).isEqualTo(hospitalReceiptReviewCreateData.getMedicalExpense())
        );
        assertThat(hospital.getTotalReceiptReviewCount()).isEqualTo(totalHospitalReceiptReviewCount);
        assertThat(doctor.getTotalReceiptReviewCount()).isEqualTo(totalDoctorReceiptReviewCount);
    }

    @DisplayName("영수증 리뷰 도움돼요 테스트")
    @Test
    void mark_receipt_review_as_helpful_test() {
        //given
        String userId = "userId";
        String hospitalReceiptReviewId = "hospitalReceiptReviewId";
        boolean help = true;
        int helpCount = 10;
        HospitalReceiptReview hospitalReceiptReview = toHospitalReceiptReview().build();
        given(hospitalReceiptReviewRepository.findById(hospitalReceiptReviewId)).willReturn(Optional.of(hospitalReceiptReview));
        given(hospitalReceiptReviewHelpRepository.countHelp(hospitalReceiptReviewId)).willReturn(helpCount);

        //when
        hospitalService.markReceiptReviewAsHelpful(userId, hospitalReceiptReviewId, help);

        //then
        then(hospitalReceiptReviewHelpRepository).should(times(1)).save(hospitalReceiptReviewHelpArgumentCaptor.capture());
        HospitalReceiptReviewHelp hospitalReceiptReviewHelp = hospitalReceiptReviewHelpArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(hospitalReceiptReviewHelp.getId().getUser().getId()).isEqualTo(userId),
                () -> assertThat(hospitalReceiptReviewHelp.getId().getHospitalReceiptReview().getId()).isEqualTo(hospitalReceiptReviewId),
                () -> assertThat(hospitalReceiptReviewHelp.getDeleted()).isFalse());
        assertThat(hospitalReceiptReview.getHelpCount()).isEqualTo(helpCount);
    }

    @DisplayName("영수증 리뷰 수정 테스트")
    @Test
    void update_receipt_review_test() {
        //given
        String userId = "userId";
        String hospitalReceiptReviewId = "hospitalReceiptReviewId";
        HospitalReceiptReview hospitalReceiptReview = toHospitalReceiptReview().id(hospitalReceiptReviewId).user(toUser().id(userId).build()).build();
        HospitalReceiptReviewUpdateData hospitalReceiptReviewUpdateData = HospitalReceiptReviewUpdateData.builder()
                .content("영수증 리뷰내용리뷰내용리뷰내용리뷰내용리뷰내용 수정")
                .imageList(List.of("/images/update_image.png", "/images/update_image.jpg", "/images/update_image.jpeg"))
                .medicalExpense(17000L)
                .build();
        given(hospitalReceiptReviewRepository.findById(hospitalReceiptReviewId)).willReturn(Optional.of(hospitalReceiptReview));

        //when
        hospitalService.updateReceiptReview(userId, hospitalReceiptReviewId, hospitalReceiptReviewUpdateData);

        //then
        assertThat(hospitalReceiptReview.getContent()).isEqualTo(hospitalReceiptReviewUpdateData.getContent());
        assertThat(hospitalReceiptReview.getImages()).containsAll(hospitalReceiptReviewUpdateData.getImageList());
        assertThat(hospitalReceiptReview.getMedicalExpense()).isEqualTo(hospitalReceiptReviewUpdateData.getMedicalExpense());
    }

    @DisplayName("영수증 리뷰 삭제 테스트")
    @Test
    void delete_receipt_review_test() {
        //given
        String userId = "userId";
        String hospitalReceiptReviewId = "hospitalReceiptReviewId";
        String hospitalId = "hospitalId";
        String doctorId = "doctorId";
        int totalHospitalReceiptReviewCount = 10;
        int totalDoctorReceiptReviewCount = 10;
        Hospital hospital = toHospital().id(hospitalId).build();
        Doctor doctor = toDoctor().id(doctorId).build();
        HospitalReceiptReview hospitalReceiptReview = toHospitalReceiptReview().id(hospitalReceiptReviewId).user(toUser().id(userId).build())
                .hospital(hospital)
                .doctor(doctor)
                .build();
        given(hospitalReceiptReviewRepository.findById(hospitalReceiptReviewId)).willReturn(Optional.of(hospitalReceiptReview));
        given(hospitalReceiptReviewRepository.countByHospitalId(hospitalId)).willReturn(totalHospitalReceiptReviewCount);
        given(hospitalReceiptReviewRepository.countByDoctorId(doctorId)).willReturn(totalDoctorReceiptReviewCount);
        given(hospitalRepository.findById(hospitalId)).willReturn(Optional.of(hospital));
        given(doctorRepository.findById(doctorId)).willReturn(Optional.of(doctor));

        //when
        hospitalService.deleteReceiptReview(userId, hospitalReceiptReviewId);

        //then
        assertThat(hospitalReceiptReview.getDeleted()).isTrue();
        assertThat(hospital.getTotalReceiptReviewCount()).isEqualTo(totalHospitalReceiptReviewCount);
        assertThat(doctor.getTotalReceiptReviewCount()).isEqualTo(totalDoctorReceiptReviewCount);
    }

    @DisplayName("병원 영수증 리뷰 중복 여부 테스트 - 실패: 중복되는 영수증 ID")
    @Test
    void validate_duplicated_hospital_receipt_review_test_fail() {
        //given
        String userId = "userId";
        String receiptId = "receiptId";
        given(hospitalReceiptReviewRepository.findByUserIdAndReceiptId(userId, receiptId))
                .willReturn(Optional.of(toHospitalReceiptReview().build()));

        //when, then
        assertThatThrownBy(() -> hospitalService.validateDuplicatedHospitalReceiptReview(userId, receiptId))
                .extracting("errorCode")
                .isEqualTo(ALREADY_EXISTS_HOSPITAL_RECEIPT_REVIEW);
    }

    @DisplayName("포에이리본 병원 평가 리뷰 중복 여부 테스트 - 실패: 해당 병원에 대한 리뷰 이미 존재")
    @Test
    void validate_duplicated_hospital_evaluation_review_test_fail() {
        //given
        String userId = "userId";
        String hospitalId = "hospitalId";
        given(hospitalEvaluationReviewRepository.findByUserIdAndHospitalId(userId, hospitalId))
                .willReturn(Optional.of(toHospitalEvaluationReview().build()));

        //when, then
        assertThatThrownBy(() -> hospitalService.validateDuplicatedHospitalEvaluationReview(userId, hospitalId))
                .extracting("errorCode")
                .isEqualTo(ALREADY_EXISTS_HOSPITAL_EVALUATION_REVIEW);
    }

    @DisplayName("포에이리본 병원 평가 질문 검증 테스트 - 실패: 유효하지 않은 질문")
    @Test
    void validate_evaluation_question_fail_test_invalid_question() {
        //given
        List<Long> hospitalEvaluationQuestionIds = List.of(1L, 2L, 3L, 4L);
        Map<Long, HospitalEvaluationQuestion> hospitalEvaluationQuestionById = Map.of(
                1L, toHospitalEvaluationQuestion().id(1L).build(),
                2L, toHospitalEvaluationQuestion().id(2L).build(),
                3L, toHospitalEvaluationQuestion().id(3L).build());

        //when, then
        assertThatThrownBy(() -> hospitalService.validateEvaluationQuestion(hospitalEvaluationQuestionIds, hospitalEvaluationQuestionById))
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_HOSPITAL_EVALUATION_QUESTION);
    }

    @DisplayName("포에이리본 병원 평가 질문 검증 테스트 - 실패: 유효한 질문에 대한 답변 부재")
    @Test
    void validate_evaluation_question_fail_test_not_found_answer() {
        //given
        List<Long> hospitalEvaluationQuestionIds = List.of(1L, 2L);
        Map<Long, HospitalEvaluationQuestion> hospitalEvaluationQuestionById = Map.of(
                1L, toHospitalEvaluationQuestion().id(1L).build(),
                2L, toHospitalEvaluationQuestion().id(2L).build(),
                3L, toHospitalEvaluationQuestion().id(3L).build());

        //when, then
        assertThatThrownBy(() -> hospitalService.validateEvaluationQuestion(hospitalEvaluationQuestionIds, hospitalEvaluationQuestionById))
                .extracting("errorCode")
                .isEqualTo(REQUIRED_HOSPITAL_EVALUATION_ANSWER);
    }

    @DisplayName("영수증 리뷰 작성자 검증 테스트 - 실패: 작성자 아님")
    @Test
    void validate_receipt_reviewer_fail_test() {
        //given
        String anotherUserId = "anotherUserId";
        String userId = "userId";
        HospitalReceiptReview hospitalReceiptReview = toHospitalReceiptReview().user(toUser().id(anotherUserId).build()).build();

        //when, then
        assertThatThrownBy(() -> hospitalService.validateReceiptReviewer(hospitalReceiptReview, userId))
                .extracting("errorCode")
                .isEqualTo(FORBIDDEN_HOSPITAL_RECEIPT_REVIEW);
    }

    @DisplayName("포에이리본 병원 평가 리뷰 작성자 검증 테스트 - 실패: 작성자 아님")
    @Test
    void validate_evaluation_reviewer_fail_test() {
        //given
        String anotherUserId = "anotherUserId";
        String userId = "userId";
        HospitalEvaluationReview hospitalEvaluationReview = toHospitalEvaluationReview().user(toUser().id(anotherUserId).build()).build();

        //when, then
        assertThatThrownBy(() -> hospitalService.validateEvaluationReviewer(hospitalEvaluationReview, userId))
                .extracting("errorCode")
                .isEqualTo(FORBIDDEN_HOSPITAL_EVALUATION_REVIEW);
    }
}
