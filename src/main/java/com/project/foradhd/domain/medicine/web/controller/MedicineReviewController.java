package com.project.foradhd.domain.medicine.web.controller;

import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineReviewMapper;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserPrivacyRepository;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.global.AuthUserId;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/medicines/reviews")
public class MedicineReviewController {

    @Qualifier("medicineReviewService")
    private final MedicineReviewService reviewService;

    @Qualifier("medicineReviewMapper")
    private final MedicineReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<MedicineReviewResponse> createReview(@RequestBody MedicineReviewRequest request, @AuthUserId String userId) {
        MedicineReview review = reviewService.createReview(request, userId);
        MedicineReviewResponse response = reviewMapper.toResponseDto(review);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/help")
    public ResponseEntity<Void> toggleHelpCount(@PathVariable Long id, @AuthUserId String userId) {
        reviewService.toggleHelpCount(id, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineReviewResponse> updateReview(@PathVariable Long id, @RequestBody MedicineReviewRequest request, @AuthUserId String userId) {
        MedicineReview review = reviewService.updateReview(id, request, userId);
        MedicineReviewResponse response = reviewMapper.toResponseDto(review);
        return ResponseEntity.ok(response);
    }


    @GetMapping("")
    public ResponseEntity<Page<MedicineReviewResponse>> getReviews(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MedicineReview> reviews = reviewService.findReviews(pageable);
        Page<MedicineReviewResponse> reviewDtos = reviews.map(reviewMapper::toResponseDto);
        return ResponseEntity.ok(reviewDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<MedicineReviewResponse>> getUserReviews(@AuthUserId String userId, Pageable pageable) {
        Page<MedicineReview> reviews = reviewService.findReviewsByUserId(userId, pageable);
        Page<MedicineReviewResponse> reviewDtos = reviews.map(reviewMapper::toResponseDto);
        return ResponseEntity.ok(reviewDtos);
    }
}
