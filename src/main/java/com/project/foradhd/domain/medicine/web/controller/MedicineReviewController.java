package com.project.foradhd.domain.medicine.web.controller;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineReviewMapper;
import com.project.foradhd.domain.user.persistence.repository.UserPrivacyRepository;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.global.AuthUserId;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/medicines/reviews")
public class MedicineReviewController {

    private final MedicineReviewService reviewService;
    private final MedicineReviewMapper reviewMapper;
    private final UserProfileRepository userProfileRepository;
    private final UserPrivacyRepository userPrivacyRepository;

    @PostMapping
    public ResponseEntity<MedicineReviewResponse> createReview(@RequestBody MedicineReviewRequest request, @AuthUserId String userId) {
        MedicineReview review = reviewService.createReview(request, userId);
        MedicineReviewResponse response = reviewMapper.toResponseDto(review, userProfileRepository, userPrivacyRepository);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineReviewResponse> updateReview(@PathVariable Long id, @RequestBody MedicineReviewRequest request, @AuthUserId String userId) {
        MedicineReview review = reviewService.updateReview(id, request, userId);
        MedicineReviewResponse response = reviewMapper.toResponseDto(review, userProfileRepository, userPrivacyRepository);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<MedicineReviewResponse.PagedMedicineReviewResponse> getReviews(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MedicineReview> reviews = reviewService.findReviews(pageable);
        List<MedicineReviewResponse> reviewDtos = reviews.stream()
                .map(review -> reviewMapper.toResponseDto(review, userProfileRepository, userPrivacyRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(reviews);
        MedicineReviewResponse.PagedMedicineReviewResponse response = MedicineReviewResponse.PagedMedicineReviewResponse.builder()
                .data(reviewDtos)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-reviews")
    public ResponseEntity<MedicineReviewResponse.PagedMedicineReviewResponse> getUserReviews(
            @AuthUserId String userId,
            @RequestParam(defaultValue = "NEWEST_FIRST") SortOption sortOption,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MedicineReview> reviews = reviewService.findReviewsByUserId(userId, pageable, sortOption);
        List<MedicineReviewResponse> reviewDtos = reviews.stream()
                .map(review -> reviewMapper.toResponseDto(review, userProfileRepository, userPrivacyRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(reviews);
        MedicineReviewResponse.PagedMedicineReviewResponse response = MedicineReviewResponse.PagedMedicineReviewResponse.builder()
                .data(reviewDtos)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<MedicineReviewResponse.PagedMedicineReviewResponse> getReviewsByMedicineId(
            @PathVariable Long medicineId,
            @RequestParam(defaultValue = "NEWEST_FIRST") SortOption sortOption,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MedicineReview> reviews = reviewService.findReviewsByMedicineId(medicineId, pageable, sortOption);
        List<MedicineReviewResponse> reviewDtos = reviews.stream()
                .map(review -> reviewMapper.toResponseDto(review, userProfileRepository, userPrivacyRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(reviews);
        MedicineReviewResponse.PagedMedicineReviewResponse response = MedicineReviewResponse.PagedMedicineReviewResponse.builder()
                .data(reviewDtos)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/help")
    public ResponseEntity<Void> toggleHelpCount(@PathVariable Long id, @AuthUserId String userId) {
        reviewService.toggleHelpCount(id, userId);
        return ResponseEntity.ok().build();
    }
}
