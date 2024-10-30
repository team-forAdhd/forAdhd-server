package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.medicine.business.service.impl.MedicineReviewServiceImpl;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewLikeRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineReviewRepository;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static com.project.foradhd.domain.medicine.fixtures.MedicineFixtures.toMedicine;
import static com.project.foradhd.domain.medicine.fixtures.MedicineFixtures.toMedicineReview;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MedicineReviewServiceImplTest {

    @Mock
    MedicineReviewRepository medicineReviewRepository;

    @Mock
    MedicineRepository medicineRepository;

    @Mock
    MedicineReviewLikeRepository medicineReviewLikeRepository;

    @Mock
    UserService userService;

    @InjectMocks
    MedicineReviewServiceImpl medicineReviewService;

    @Captor
    ArgumentCaptor<MedicineReview> medicineReviewArgumentCaptor;

    @Test
    void createReview_shouldReturnCreatedReview() {
        //given
        String userId = "user123";
        MedicineReviewRequest request = MedicineReviewRequest.builder()
                .medicineId(1L)
                .content("This is a review.")
                .grade(4.5)
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
        given(medicineReviewRepository.save(any(MedicineReview.class))).willReturn(review);

        //when
        MedicineReview createdReview = medicineReviewService.createReview(request, userId);

        //then
        assertThat(createdReview).isNotNull();
        assertThat(createdReview.getContent()).isEqualTo("This is a review.");
        verify(medicineReviewRepository).save(any(MedicineReview.class));
    }

    @Test
    void toggleHelpCount_shouldIncrementHelpCount() {
        //given
        Long reviewId = 1L;
        String userId = "user123";
        User user = User.builder().id(userId).build();
        MedicineReview review = MedicineReview.builder()
                .id(reviewId)
                .helpCount(1)
                .build();

        given(medicineReviewRepository.findById(reviewId)).willReturn(Optional.of(review));
        given(userService.getUser(userId)).willReturn(user);
        given(medicineReviewLikeRepository.existsByUserIdAndMedicineReviewId(userId, reviewId)).willReturn(false);

        //when
        medicineReviewService.toggleHelpCount(reviewId, userId);

        //then
        assertThat(review.getHelpCount()).isEqualTo(2);
    }

    @Test
    void updateReview_shouldUpdateExistingReview() {
        //given
        Long reviewId = 1L;
        String userId = "user123";
        MedicineReviewRequest request = MedicineReviewRequest.builder()
                .medicineId(1L)
                .content("Updated review.")
                .grade(5.0)
                .build();

        Medicine medicine = Medicine.builder().id(1L).build();
        User user = User.builder().id(userId).build();
        MedicineReview existingReview = MedicineReview.builder()
                .id(reviewId)
                .user(user)
                .content("Old review")
                .build();

        given(medicineReviewRepository.findById(reviewId)).willReturn(Optional.of(existingReview));
        given(medicineRepository.findById(request.getMedicineId())).willReturn(Optional.of(medicine));
        given(medicineReviewRepository.save(any(MedicineReview.class))).willReturn(existingReview);

        //when
        medicineReviewService.updateReview(reviewId, request, userId);

        //then
        verify(medicineReviewRepository).save(medicineReviewArgumentCaptor.capture());
        MedicineReview updatedReview = medicineReviewArgumentCaptor.getValue();
        assertThat(updatedReview.getContent()).isEqualTo("Updated review.");
    }

    @Test
    void findReviewsByUserId_shouldReturnPagedReviews() {
        //given
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);
        SortOption sortOption = SortOption.NEWEST_FIRST;
        MedicineReview review = MedicineReview.builder()
                .id(1L)
                .content("This is a review.")
                .build();

        Page<MedicineReview> reviewPage = new PageImpl<>(Arrays.asList(review), pageable, 1);
        given(medicineReviewRepository.findByUserIdWithDetails(eq(userId), any(Pageable.class))).willReturn(reviewPage);

        //when
        Page<MedicineReview> result = medicineReviewService.findReviewsByUserId(userId, pageable, sortOption);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(medicineReviewRepository).findByUserIdWithDetails(eq(userId), any(Pageable.class));
    }

    @Test
    void deleteReview_shouldDeleteReview() {
        //given
        String userId = "userId";
        Long medicineId = 1L;
        Long medicineReviewId = 1L;
        User user = toUser().id(userId).build();
        Medicine medicine = toMedicine().id(medicineId).build();
        MedicineReview medicineReview = toMedicineReview().id(medicineReviewId)
                .user(user)
                .medicine(medicine)
                .build();

        given(medicineReviewRepository.findById(medicineReviewId)).willReturn(Optional.of(medicineReview));
        willDoNothing().given(medicineReviewRepository).deleteById(medicineReviewId);

        //when
        medicineReviewService.deleteReview(medicineReviewId, userId);

        //then
        verify(medicineReviewRepository).deleteById(medicineReviewId);
    }
}
