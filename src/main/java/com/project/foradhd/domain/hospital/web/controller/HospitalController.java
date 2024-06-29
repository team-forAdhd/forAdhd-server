package com.project.foradhd.domain.hospital.web.controller;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.*;
import com.project.foradhd.domain.hospital.business.service.HospitalService;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalListNearbyRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewCreateRequest;
import com.project.foradhd.domain.hospital.web.dto.request.HospitalReceiptReviewUpdateRequest;
import com.project.foradhd.domain.hospital.web.dto.response.*;
import com.project.foradhd.domain.hospital.web.mapper.HospitalMapper;
import com.project.foradhd.global.AuthUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/hospitals")
@RestController
public class HospitalController {

    private final HospitalService hospitalService;
    private final HospitalMapper hospitalMapper;

    @GetMapping("/nearby")
    public ResponseEntity<HospitalListNearbyResponse> getHospitalListNearby(@AuthUserId String userId,
                                                                            @ModelAttribute HospitalListNearbyRequest request,
                                                                            Pageable pageable) {
        HospitalListNearbySearchCond searchCond = hospitalMapper.mapToSearchCond(request);
        HospitalListNearbyData hospitalListNearbyData = hospitalService.getHospitalListNearby(userId, searchCond, pageable);
        HospitalListNearbyResponse hospitalListNearbyResponse = hospitalMapper.toHospitalListNearbyResponse(hospitalListNearbyData);
        return ResponseEntity.ok(hospitalListNearbyResponse);
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<HospitalDetailsResponse> getHospitalDetails(@AuthUserId String userId,
                                                                    @PathVariable String hospitalId) {
        HospitalDetailsData hospitalDetailsData = hospitalService.getHospitalDetails(userId, hospitalId);
        HospitalDetailsResponse hospitalDetailsResponse = hospitalMapper.toHospitalDetailsResponse(hospitalDetailsData);
        return ResponseEntity.ok(hospitalDetailsResponse);
    }

    @GetMapping("/{hospitalId}/doctors/{doctorId}")
    public ResponseEntity<DoctorDetailsResponse> getDoctorDetails(@PathVariable String hospitalId, @PathVariable String doctorId) {
        DoctorDetailsData doctorDetailsData = hospitalService.getDoctorDetails(hospitalId, doctorId);
        DoctorDetailsResponse doctorDetailsResponse = hospitalMapper.toDoctorDetailsResponse(doctorDetailsData);
        return ResponseEntity.ok(doctorDetailsResponse);
    }

    @GetMapping("/{hospitalId}/doctors/{doctorId}/receipt-reviews")
    public ResponseEntity<HospitalReceiptReviewListResponse> getReceiptReviewList(@AuthUserId String userId,
                                                                                @PathVariable String hospitalId, @PathVariable String doctorId,
                                                                                Pageable pageable) {
        HospitalReceiptReviewListData hospitalReceiptReviewListData = hospitalService.getReceiptReviewList(userId, hospitalId, doctorId, pageable);
        HospitalReceiptReviewListResponse hospitalReceiptReviewListResponse =
                hospitalMapper.toReceiptReviewListResponse(hospitalReceiptReviewListData);
        return ResponseEntity.ok(hospitalReceiptReviewListResponse);
    }

    @GetMapping("/evaluation-questions")
    public ResponseEntity<HospitalEvaluationQuestionListResponse> getEvaluationQuestionList() {
        HospitalEvaluationQuestionListData hospitalEvaluationQuestionListData = hospitalService.getEvaluationQuestionList();
        HospitalEvaluationQuestionListResponse hospitalEvaluationQuestionListResponse =
                hospitalMapper.toEvaluationQuestionListResponse(hospitalEvaluationQuestionListData);
        return ResponseEntity.ok(hospitalEvaluationQuestionListResponse);
    }

    @PostMapping("/{hospitalId}/bookmark")
    public ResponseEntity<Void> bookmarkHospital(@AuthUserId String userId,
                                                @PathVariable String hospitalId,
                                                @RequestParam Boolean bookmark) {
        hospitalService.saveHospitalBookmark(userId, hospitalId, bookmark);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{hospitalId}/receipt-reviews")
    public ResponseEntity<Void> createHospitalReceiptReview(@AuthUserId String userId,
                                                    @PathVariable String hospitalId,
                                                    @RequestBody @Valid HospitalReceiptReviewCreateRequest request) {
        HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData = hospitalMapper.toHospitalReceiptReviewCreateData(request);
        hospitalService.createReceiptReview(userId, hospitalId, hospitalReceiptReviewCreateData);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{hospitalId}/doctors/{doctorId}/receipt-reviews")
    public ResponseEntity<Void> createDoctorReceiptReview(@AuthUserId String userId,
                                                    @PathVariable String hospitalId, @PathVariable String doctorId,
                                                    @RequestBody @Valid HospitalReceiptReviewCreateRequest request) {
        HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData = hospitalMapper.toHospitalReceiptReviewCreateData(request);
        hospitalService.createReceiptReview(userId, hospitalId, doctorId, hospitalReceiptReviewCreateData);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/receipt-reviews/{hospitalReceiptReviewId}/help")
    public ResponseEntity<Void> markReceiptReviewAsHelpful(@AuthUserId String userId,
                                                        @PathVariable String hospitalReceiptReviewId,
                                                        @RequestParam Boolean help) {
        hospitalService.markReceiptReviewAsHelpful(userId, hospitalReceiptReviewId, help);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/receipt-reviews/{hospitalReceiptReviewId}")
    public ResponseEntity<Void> updateReceiptReview(@AuthUserId String userId,
                                                    @PathVariable String hospitalReceiptReviewId,
                                                    @RequestBody @Valid HospitalReceiptReviewUpdateRequest request) {
        HospitalReceiptReviewUpdateData hospitalReceiptReviewUpdateData = hospitalMapper.toHospitalReceiptReviewUpdateData(request);
        hospitalService.updateReceiptReview(userId, hospitalReceiptReviewId, hospitalReceiptReviewUpdateData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/receipt-reviews/{hospitalReceiptReviewId}")
    public ResponseEntity<Void> deleteReceiptReview(@AuthUserId String userId,
                                                    @PathVariable String hospitalReceiptReviewId) {
        hospitalService.deleteReceiptReview(userId, hospitalReceiptReviewId);
        return ResponseEntity.ok().build();
    }
}
