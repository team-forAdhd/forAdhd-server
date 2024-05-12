package com.project.foradhd.domain.hospital.web.dto.request;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalBriefReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewCreateData;
import com.project.foradhd.domain.hospital.business.dto.in.HospitalReceiptReviewUpdateData;
import com.project.foradhd.domain.hospital.business.dto.out.HospitalDetailsData;
import com.project.foradhd.domain.hospital.business.service.HospitalService;
import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReview;
import com.project.foradhd.domain.hospital.web.dto.response.DoctorDetailsResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalDetailsResponse;
import com.project.foradhd.domain.hospital.web.dto.response.HospitalReceiptReviewListResponse;
import com.project.foradhd.domain.hospital.web.mapper.HospitalMapper;
import com.project.foradhd.global.AuthUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/hospitals")
@RestController
public class HospitalController {

    private final HospitalService hospitalService;
    private final HospitalMapper hospitalMapper;

    @GetMapping("/nearby")
    public ResponseEntity<Void> getHospitalListNearby() {
        return ResponseEntity.ok().build();
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
        Doctor doctor = hospitalService.getDoctorDetails(doctorId);
        DoctorDetailsResponse doctorDetailsResponse = hospitalMapper.toDoctorDetailsResponse(doctorId);
        return ResponseEntity.ok(doctorDetailsResponse);
    }

    @GetMapping("/{hospitalId}/doctors/{doctorId}/receipt-reviews")
    public ResponseEntity<HospitalReceiptReviewListResponse> getReceiptReviewList(@PathVariable String hospitalId, @PathVariable String doctorId) {
        List<HospitalReceiptReview> receiptReviewList = hospitalService.getReceiptReviewList(hospitalId, doctorId);
        HospitalReceiptReviewListResponse hospitalReceiptReviewListResponse =
                hospitalMapper.toReceiptReviewListResponse(receiptReviewList);
        return ResponseEntity.ok(hospitalReceiptReviewListResponse);
    }

    @PostMapping("/{hospitalId}/bookmark")
    public ResponseEntity<Void> bookmarkHospital(@PathVariable String hospitalId,
                                                @RequestParam Boolean bookmark) {
        hospitalService.bookmarkHospital(hospitalId, bookmark);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{hospitalId}/doctors/{doctorId}/brief-reviews")
    public ResponseEntity<Void> createBriefReview(@PathVariable String hospitalId, @PathVariable String doctorId,
                                                @RequestBody @Valid HospitalBriefReviewCreateRequest request) {
        HospitalBriefReviewCreateData hospitalBriefReviewCreateData = hospitalMapper.toHospitalBriefReviewCreateData(request);
        hospitalService.createBriefReview(hospitalBriefReviewCreateData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/doctors/brief-reviews/{hospitalBriefReviewId}")
    public ResponseEntity<Void> deleteBriefReview(@PathVariable String hospitalBriefReviewId) {
        hospitalService.deleteBriefReview(hospitalBriefReviewId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{hospitalId}/doctors/{doctorId}/receipt-reviews")
    public ResponseEntity<Void> createReceiptReview(@PathVariable String hospitalId, @PathVariable String doctorId,
                                                    @RequestBody @Valid HospitalReceiptReviewCreateRequest request) {
        HospitalReceiptReviewCreateData hospitalReceiptReviewCreateData = hospitalMapper.toHospitalReceiptReviewCreateData(request);
        hospitalService.createReceiptReview(hospitalReceiptReviewCreateData);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/doctors/receipt-reviews/{hospitalReceiptReviewId}/help")
    public ResponseEntity<Void> markReceiptReviewAsHelpful(@PathVariable String hospitalReceiptReviewId,
                                                        @RequestParam Boolean help) {
        hospitalService.markReceiptReviewAsHelpful(hospitalReceiptReviewId, help);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/doctors/receipt-reviews/{hospitalReceiptReviewId}")
    public ResponseEntity<Void> updateReceiptReview(@PathVariable String hospitalReceiptReviewId,
                                                    @RequestBody @Valid HospitalReceiptReviewUpdateRequest request) {
        HospitalReceiptReviewUpdateData hospitalReceiptReviewUpdateData = hospitalMapper.toHospitalReceiptReviewUpdateData(request);
        hospitalService.updateReceiptReview(hospitalReceiptReviewId, hospitalReceiptReviewUpdateData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/doctors/receipt-reviews/{hospitalReceiptReviewId}")
    public ResponseEntity<Void> deleteReceiptReview(@PathVariable String hospitalReceiptReviewId) {
        hospitalService.deleteReceiptReview(hospitalReceiptReviewId);
        return ResponseEntity.ok().build();
    }
}
