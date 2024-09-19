package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewLikeRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MedicineReviewServiceImplTest {

    @Mock
    private MedicineReviewRepository reviewRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineReviewLikeRepository reviewLikeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MedicineReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview_shouldReturnCreatedReview() {
        // given
        String userId = "user123";
        MedicineReviewRequest request = MedicineReviewRequest.builder()
                .medicineId(1L)
                .content("This is a review.")
                .grade(4.5f)
                .images(Arrays.asList("image1", "image2"))
                .build();

        User user = User.builder().id(userId).build();
        Medicine medicine = Medicine.builder().id(1L).build();

        MedicineReview review = MedicineReview.builder()
                .user(user)
                .medicine(medicine)
                .content(request.getContent())
                .grade(request.getGrade())
                .images(request.getImages())
                .coMedications(Arrays.asList(1L, 2L))
                .build();

        given(userService.getUser(userId)).willReturn(user);
        given(medicineRepository.findById(request.getMedicineId())).willReturn(Optional.of(medicine));
        given(reviewRepository.save(review)).willReturn(review);

        // when
        MedicineReview createdReview = reviewService.createReview(request, userId);

        // then
        assertThat(createdReview).isNotNull();
        assertThat(createdReview.getContent()).isEqualTo("This is a review.");
        verify(reviewRepository).save(review);
    }

    @Test
    void toggleHelpCount_shouldIncrementHelpCount() {
        // given
        Long reviewId = 1L;
        String userId = "user123";
        User user = User.builder().id(userId).build();
        MedicineReview review = MedicineReview.builder()
                .id(reviewId)
                .helpCount(1)
                .build();

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
        given(userService.getUser(userId)).willReturn(user);
        given(reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId)).willReturn(false);
        given(reviewRepository.save(review)).willReturn(review);

        // when
        reviewService.toggleHelpCount(reviewId, userId);

        // then
        assertThat(review.getHelpCount()).isEqualTo(2);
        verify(reviewRepository).save(review);
    }

    @Test
    void updateReview_shouldUpdateExistingReview() {
        // given
        Long reviewId = 1L;
        String userId = "user123";
        MedicineReviewRequest request = MedicineReviewRequest.builder()
                .medicineId(1L)
                .content("Updated review.")
                .grade(5.0f)
                .build();

        MedicineReview existingReview = MedicineReview.builder()
                .id(reviewId)
                .content("Old review")
                .build();

        Medicine medicine = Medicine.builder().id(1L).build();
        User user = User.builder().id(userId).build();

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(existingReview));
        given(userService.getUser(userId)).willReturn(user);
        given(medicineRepository.findById(request.getMedicineId())).willReturn(Optional.of(medicine));
        given(reviewRepository.save(existingReview)).willReturn(existingReview);

        // when
        MedicineReview updatedReview = reviewService.updateReview(reviewId, request, userId);

        // then
        assertThat(updatedReview.getContent()).isEqualTo("Updated review.");
        verify(reviewRepository).save(existingReview);
    }

    @Test
    void findReviewsByUserId_shouldReturnPagedReviews() {
        // given
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);
        SortOption sortOption = SortOption.NEWEST_FIRST;
        MedicineReview review = MedicineReview.builder()
                .id(1L)
                .content("This is a review.")
                .build();

        Page<MedicineReview> reviewPage = new PageImpl<>(Arrays.asList(review), pageable, 1);
        given(reviewRepository.findByUserIdWithDetails(userId, pageable)).willReturn(reviewPage);

        // when
        Page<MedicineReview> result = reviewService.findReviewsByUserId(userId, pageable, sortOption);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(reviewRepository).findByUserIdWithDetails(userId, pageable);
    }

    @Test
    void deleteReview_shouldDeleteReview() {
        // given
        Long reviewId = 1L;
        given(reviewRepository.existsById(reviewId)).willReturn(true);
        willDoNothing().given(reviewRepository).deleteById(reviewId);

        // when
        reviewService.deleteReview(reviewId);

        // then
        verify(reviewRepository).deleteById(reviewId);
    }
}
