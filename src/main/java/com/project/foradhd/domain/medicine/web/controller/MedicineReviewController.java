package com.project.foradhd.domain.medicine.web.controller;

import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineReviewResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medicine/reviews")
public class MedicineReviewController {
    private final MedicineReviewService reviewService;
    private final MedicineReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<MedicineReviewResponse> createReview(@RequestBody MedicineReviewRequest request) {
        MedicineReview review = reviewService.createReview(request);
        return ResponseEntity.ok(reviewMapper.toResponseDto(review));
    }

    @PostMapping("/{id}/help")
    public ResponseEntity<Void> incrementHelpCount(@PathVariable Long id) {
        reviewService.incrementHelpCount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineReviewResponse> updateReview(@PathVariable Long id, @RequestBody MedicineReviewRequest request) {
        MedicineReview review = reviewService.updateReview(id, request);
        return ResponseEntity.ok(reviewMapper.toResponseDto(review));
    }
}
