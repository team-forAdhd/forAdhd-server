package com.project.foradhd.domain.medicine.business.service.Impl;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MedicineReviewServiceImplTest {

    @Mock
    private MedicineReviewRepository reviewRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MedicineReviewServiceImpl reviewService;

    private MedicineReviewRequest request;
    private MedicineReview review;
    private Medicine medicine;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user1")
                .build();

        medicine = Medicine.builder()
                .id(1L)
                .build();

        request = MedicineReviewRequest.builder()
                .userId("user1")
                .medicineId(1L)
                .content("Great medicine!")
                .grade(5)
                .images(Collections.emptyList())
                .coMedications(Collections.emptyList())
                .build();

        review = MedicineReview.builder()
                .id(1L)
                .user(user)
                .medicine(medicine)
                .content("Great medicine!")
                .grade(5)
                .images(Collections.emptyList())
                .coMedications(Collections.emptyList())
                .helpCount(0)
                .build();
    }

    @Test
    void createReview_ShouldReturnSavedReview() {
        // given
        given(userService.getUser("user1")).willReturn(user);
        given(medicineRepository.findById(1L)).willReturn(Optional.of(medicine));
        given(reviewRepository.save(any(MedicineReview.class))).willReturn(review);

        // when
        MedicineReview createdReview = reviewService.createReview(request);

        // then
        assertThat(createdReview).isNotNull();
        assertThat(createdReview.getUser().getId()).isEqualTo("user1");
        assertThat(createdReview.getMedicine().getId()).isEqualTo(1L);
        assertThat(createdReview.getContent()).isEqualTo("Great medicine!");
    }

    @Test
    void createReview_ShouldThrowException_WhenUserNotFound() {
        // given
        given(userService.getUser("user1")).willReturn(null);

        // when
        Throwable thrown = catchThrowable(() -> reviewService.createReview(request));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    void createReview_ShouldThrowException_WhenMedicineNotFound() {
        // given
        given(userService.getUser("user1")).willReturn(user);
        given(medicineRepository.findById(1L)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> reviewService.createReview(request));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_MEDICINE.getMessage());
    }

    @Test
    void incrementHelpCount_ShouldIncrementHelpCount() {
        // given
        given(reviewRepository.findById(1L)).willReturn(Optional.of(review));
        MedicineReview updatedReview = review.toBuilder().helpCount(1).build();
        given(reviewRepository.save(any(MedicineReview.class))).willReturn(updatedReview);

        // when
        reviewService.incrementHelpCount(1L);

        // then
        assertThat(updatedReview.getHelpCount()).isEqualTo(1);
    }

    @Test
    void incrementHelpCount_ShouldThrowException_WhenReviewNotFound() {
        // given
        given(reviewRepository.findById(1L)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> reviewService.incrementHelpCount(1L));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_MEDICINE_REVIEW.getMessage());
    }

    @Test
    void updateReview_ShouldReturnUpdatedReview() {
        // given
        given(reviewRepository.findById(1L)).willReturn(Optional.of(review));
        given(medicineRepository.findById(1L)).willReturn(Optional.of(medicine));
        given(reviewRepository.save(any(MedicineReview.class))).willReturn(review);

        // when
        MedicineReview updatedReview = reviewService.updateReview(1L, request);

        // then
        assertThat(updatedReview).isNotNull();
        assertThat(updatedReview.getMedicine().getId()).isEqualTo(1L);
        assertThat(updatedReview.getContent()).isEqualTo("Great medicine!");
    }

    @Test
    void updateReview_ShouldThrowException_WhenReviewNotFound() {
        // given
        given(reviewRepository.findById(1L)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> reviewService.updateReview(1L, request));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_MEDICINE_REVIEW.getMessage());
    }

    @Test
    void updateReview_ShouldThrowException_WhenMedicineNotFound() {
        // given
        given(reviewRepository.findById(1L)).willReturn(Optional.of(review));
        given(medicineRepository.findById(1L)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> reviewService.updateReview(1L, request));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_MEDICINE.getMessage());
    }

    @Test
    void deleteReview_ShouldDeleteReview() {
        // given
        given(reviewRepository.existsById(1L)).willReturn(true);
        willDoNothing().given(reviewRepository).deleteById(1L);

        // when
        reviewService.deleteReview(1L);

        // then
        then(reviewRepository).should().deleteById(1L);
    }

    @Test
    void deleteReview_ShouldThrowException_WhenReviewNotFound() {
        // given
        given(reviewRepository.existsById(1L)).willReturn(false);

        // when
        Throwable thrown = catchThrowable(() -> reviewService.deleteReview(1L));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_MEDICINE_REVIEW.getMessage());
    }

    @Test
    void findReviews_ShouldReturnPageOfReviews() {
        // given
        Pageable pageable = Pageable.unpaged();
        Page<MedicineReview> reviewPage = new PageImpl<>(Collections.singletonList(review));
        given(reviewRepository.findAll(pageable)).willReturn(reviewPage);

        // when
        Page<MedicineReview> result = reviewService.findReviews(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getContent()).isEqualTo("Great medicine!");
    }

    @Test
    void findReviewsByUserId_ShouldReturnPageOfReviews() {
        // given
        Pageable pageable = Pageable.unpaged();
        Page<MedicineReview> reviewPage = new PageImpl<>(Collections.singletonList(review));
        given(reviewRepository.findByUserId("user1", pageable)).willReturn(reviewPage);

        // when
        Page<MedicineReview> result = reviewService.findReviewsByUserId("user1", pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getContent()).isEqualTo("Great medicine!");
    }
}
