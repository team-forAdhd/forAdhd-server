package com.project.foradhd.domain.medicine.web.controller;

import com.project.foradhd.domain.auth.business.service.JwtService;
import com.project.foradhd.domain.medicine.business.service.MedicineReviewService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineReview;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineReviewRequest;
import com.project.foradhd.domain.medicine.web.mapper.MedicineReviewMapper;
import com.project.foradhd.domain.user.persistence.repository.UserPrivacyRepository;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.global.util.HeaderUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

@WebMvcTest(MedicineReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MedicineReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicineReviewService medicineReviewService;

    @MockBean
    private HeaderUtil headerUtil;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MedicineReviewMapper medicineReviewMapper;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @MockBean
    private UserPrivacyRepository userPrivacyRepository;

    @Test
    public void getReviews_shouldReturnPagedReviews() throws Exception {
        // Mock 데이터를 설정합니다.
        MedicineReview mockReview = MedicineReview.builder()
                .id(1L) // ID를 명시적으로 추가
                .content("This is a test review.") // 리뷰 내용 설정
                .grade(4.5f) // 평점 설정
                .images(Collections.emptyList()) // 이미지 리스트 설정
                .coMedications(Collections.emptyList()) // 함께 복용한 약 리스트 설정
                .build();

        // Paged 데이터 생성
        Page<MedicineReview> pagedReviews = new PageImpl<>(List.of(mockReview));

        // Service에서 findReviews 메서드가 호출될 때 pagedReviews 반환
        given(medicineReviewService.findReviews(any(Pageable.class)))
                .willReturn(pagedReviews);

        // 테스트 요청
        mockMvc.perform(get("/api/v1/medicines/reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].content").value("This is a test review.")) // content 필드 검증
                .andExpect(jsonPath("$.data[0].grade").value(4.5)); // grade 필드 검증
    }

    @Test
    public void createReview_shouldReturnCreatedReview() throws Exception {
        // Mock 데이터를 설정합니다.
        MedicineReviewRequest reviewRequest = MedicineReviewRequest.builder()
                .medicineId(1L)
                .content("This is a review.")
                .grade(4.5f)
                .build();

        MedicineReview mockReview = MedicineReview.builder()
                .content("This is a review.")
                .grade(4.5f)
                .build();

        // Service에서 createReview 메서드가 호출될 때 mockReview 반환
        given(medicineReviewService.createReview(any(MedicineReviewRequest.class), anyString()))
                .willReturn(mockReview);

        // 테스트 요청
        mockMvc.perform(post("/api/v1/medicines/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"medicineId\": 1, \"content\": \"This is a review.\", \"grade\": 4.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("This is a review.")) // content 필드 검증
                .andExpect(jsonPath("$.grade").value(4.5)); // grade 필드 검증
    }
}
