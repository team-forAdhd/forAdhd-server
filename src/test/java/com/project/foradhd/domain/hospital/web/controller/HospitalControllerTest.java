package com.project.foradhd.domain.hospital.web.controller;

import com.project.foradhd.config.SecurityTestConfig;
import com.project.foradhd.config.user.WithMockTestUser;
import com.project.foradhd.domain.hospital.business.dto.out.*;
import com.project.foradhd.domain.hospital.business.service.HospitalOperationService;
import com.project.foradhd.domain.hospital.business.service.HospitalService;
import com.project.foradhd.domain.hospital.fixtures.HospitalFixtures;
import com.project.foradhd.domain.hospital.persistence.entity.*;
import com.project.foradhd.domain.hospital.web.dto.request.*;
import com.project.foradhd.domain.hospital.web.enums.HospitalFilter;
import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewFilter;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewType;
import com.project.foradhd.domain.hospital.web.mapper.HospitalMapper;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import com.project.foradhd.global.util.JsonUtil;
import com.project.foradhd.global.util.TimeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.project.foradhd.domain.hospital.fixtures.HospitalFixtures.toHospitalEvaluationQuestion;
import static com.project.foradhd.domain.hospital.fixtures.HospitalFixtures.toHospitalEvaluationReview;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockTestUser
@ContextConfiguration(classes = SecurityTestConfig.class)
@WebMvcTest(value = { HospitalController.class, HospitalMapper.class})
class HospitalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HospitalService hospitalService;

    @MockBean
    HospitalOperationService hospitalOperationService;

    @DisplayName("내 주변 병원 목록 조회 컨트롤러 테스트")
    @Test
    void get_hospital_list_nearby_test() throws Exception {
        //given
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put("latitude", List.of(String.valueOf(37.4867657)));
        queryParams.put("longitude", List.of(String.valueOf(127.1031943)));
        queryParams.put("radius", List.of(String.valueOf(1000)));
        queryParams.put("filter", List.of(HospitalFilter.ALL.name()));
        queryParams.put("page", List.of(String.valueOf(0)));
        queryParams.put("size", List.of(String.valueOf(10)));
        queryParams.put("sort", List.of("reviewCount", "desc"));

        HospitalListNearbyData.HospitalNearbyData hospital1 = HospitalListNearbyData.HospitalNearbyData.builder()
                .hospital(HospitalFixtures.toHospital().id("1").name("병원1").build())
                .distance(10D)
                .isBookmarked(true)
                .build();
        HospitalListNearbyData.HospitalNearbyData hospital2 = HospitalListNearbyData.HospitalNearbyData.builder()
                .hospital(HospitalFixtures.toHospital().id("2").name("병원2").build())
                .distance(20D)
                .isBookmarked(false)
                .build();
        PagingResponse paging = PagingResponse.builder()
                .page(0)
                .size(10)
                .totalPages(1)
                .numberOfElements(2)
                .totalElements(2L)
                .isFirst(true)
                .isLast(true)
                .isEmpty(false)
                .build();
        HospitalListNearbyData hospitalListNearbyData = HospitalListNearbyData.builder()
                .hospitalList(List.of(hospital1, hospital2))
                .paging(paging)
                .build();
        Map<String, HospitalOperationStatus> operationStatusByHospital = Map.of(hospital1.getHospital().getId(), HospitalOperationStatus.OPEN,
                hospital2.getHospital().getId(), HospitalOperationStatus.UNKNOWN);
        given(hospitalService.getHospitalListNearby(anyString(), any(), any())).willReturn(hospitalListNearbyData);
        given(hospitalOperationService.getHospitalOperationStatus(any())).willReturn(operationStatusByHospital);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/nearby")
                        .queryParams(queryParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalList[0].hospitalId").value(hospital1.getHospital().getId()))
                .andExpect(jsonPath("$.hospitalList[0].name").value(hospital1.getHospital().getName()))
                .andExpect(jsonPath("$.hospitalList[0].totalReceiptReviewCount").value(hospital1.getHospital().getTotalReceiptReviewCount()))
                .andExpect(jsonPath("$.hospitalList[0].totalEvaluationReviewCount").value(hospital1.getHospital().getTotalEvaluationReviewCount()))
                .andExpect(jsonPath("$.hospitalList[0].latitude").value(hospital1.getHospital().getLocation().getY()))
                .andExpect(jsonPath("$.hospitalList[0].longitude").value(hospital1.getHospital().getLocation().getX()))
                .andExpect(jsonPath("$.hospitalList[0].distance").value(hospital1.getDistance()))
                .andExpect(jsonPath("$.hospitalList[0].operationStatus").value(operationStatusByHospital.get(hospital1.getHospital().getId()).name()))
                .andExpect(jsonPath("$.hospitalList[0].isBookmarked").value(hospital1.getIsBookmarked()))
                .andDo(print());
        then(hospitalService).should(times(1)).getHospitalListNearby(anyString(), any(), any());
        then(hospitalOperationService).should(times(1)).getHospitalOperationStatus(any());
    }
    
    @DisplayName("영수증 리뷰 목록 조회 컨트롤러 테스트")
    @Test
    void get_receipt_review_list_test() throws Exception {
        //given
        String hospitalId = "1";
        String doctorId = "1";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put("page", List.of(String.valueOf(0)));
        queryParams.put("size", List.of(String.valueOf(10)));
        queryParams.put("sort", List.of("createdAt", "desc"));
        queryParams.put("doctorId", List.of(doctorId));

        HospitalReceiptReviewListData.HospitalReceiptReviewData hospitalReceiptReviewData1 = HospitalReceiptReviewListData.HospitalReceiptReviewData.builder()
                .hospitalReceiptReview(HospitalFixtures.toHospitalReceiptReview().id("1").content("영수증 리뷰1").build())
                .writerId("1")
                .writerName("김다")
                .writerImage("v2/images/profile1.png")
                .doctorName("김코코")
                .isHelped(true)
                .isMine(true)
                .build();
        HospitalReceiptReviewListData.HospitalReceiptReviewData hospitalReceiptReviewData2 = HospitalReceiptReviewListData.HospitalReceiptReviewData.builder()
                .hospitalReceiptReview(HospitalFixtures.toHospitalReceiptReview().id("2").content("영수증 리뷰2").build())
                .writerId("2")
                .writerName("단이")
                .writerImage("v2/images/profile2.png")
                .doctorName("김코코")
                .isHelped(false)
                .isMine(false)
                .build();
        PagingResponse paging = PagingResponse.builder()
                .page(0)
                .size(10)
                .totalPages(1)
                .numberOfElements(2)
                .totalElements(2L)
                .isFirst(true)
                .isLast(true)
                .isEmpty(false)
                .build();
        HospitalReceiptReviewListData hospitalReceiptReviewListData = HospitalReceiptReviewListData.builder()
                .hospitalReceiptReviewList(List.of(hospitalReceiptReviewData1, hospitalReceiptReviewData2))
                .paging(paging)
                .build();
        given(hospitalService.getReceiptReviewList(anyString(), anyString(), anyString(), any())).willReturn(hospitalReceiptReviewListData);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/{hospitalId}/receipt-reviews", hospitalId)
                        .queryParams(queryParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].hospitalReceiptReviewId").value(hospitalReceiptReviewData1.getHospitalReceiptReview().getId()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].writerId").value(hospitalReceiptReviewData1.getWriterId()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].writerName").value(hospitalReceiptReviewData1.getWriterName()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].writerImage").value(hospitalReceiptReviewData1.getWriterImage()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].doctorName").value(hospitalReceiptReviewData1.getDoctorName()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].content").value(hospitalReceiptReviewData1.getHospitalReceiptReview().getContent()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].imageList").isArray())
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].medicalExpense").value(hospitalReceiptReviewData1.getHospitalReceiptReview().getMedicalExpense()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].helpCount").value(hospitalReceiptReviewData1.getHospitalReceiptReview().getHelpCount()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].isHelped").value(hospitalReceiptReviewData1.getIsHelped()))
                .andExpect(jsonPath("$.hospitalReceiptReviewList[0].isMine").value(hospitalReceiptReviewData1.getIsMine()))
                .andDo(print());
        then(hospitalService).should(times(1)).getReceiptReviewList(anyString(), eq(hospitalId), eq(doctorId), any());
    }
    
    @DisplayName("내가 저장한 병원 목록 조회 컨트롤러 테스트")
    @Test
    void get_hospital_list_bookmark_test() throws Exception {
        //given
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put("page", List.of(String.valueOf(0)));
        queryParams.put("size", List.of(String.valueOf(10)));
        queryParams.put("sort", List.of("createdAt", "desc"));

        Hospital hospital1 = HospitalFixtures.toHospital().id("1").name("병원1").build();
        Hospital hospital2 = HospitalFixtures.toHospital().id("2").name("병원2").build();
        PagingResponse paging = PagingResponse.builder()
                .page(0)
                .size(10)
                .totalPages(1)
                .numberOfElements(2)
                .totalElements(2L)
                .isFirst(true)
                .isLast(true)
                .isEmpty(false)
                .build();
        HospitalListBookmarkData hospitalListBookmarkData = HospitalListBookmarkData.builder()
                .hospitalList(List.of(
                        HospitalListBookmarkData.HospitalBookmarkData.builder().hospital(hospital1).build(),
                        HospitalListBookmarkData.HospitalBookmarkData.builder().hospital(hospital2).build()))
                .paging(paging)
                .build();
        Map<String, HospitalOperationStatus> operationStatusByHospital = Map.of(hospital1.getId(), HospitalOperationStatus.OPEN, hospital2.getId(), HospitalOperationStatus.UNKNOWN);
        given(hospitalService.getHospitalListBookmark(anyString(), any())).willReturn(hospitalListBookmarkData);
        given(hospitalOperationService.getHospitalOperationStatus(any())).willReturn(operationStatusByHospital);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/bookmark")
                        .queryParams(queryParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalList[0].hospitalId").value(hospital1.getId()))
                .andExpect(jsonPath("$.hospitalList[0].name").value(hospital1.getName()))
                .andExpect(jsonPath("$.hospitalList[0].totalReceiptReviewCount").value(hospital1.getTotalReceiptReviewCount()))
                .andExpect(jsonPath("$.hospitalList[0].totalEvaluationReviewCount").value(hospital1.getTotalEvaluationReviewCount()))
                .andExpect(jsonPath("$.hospitalList[0].operationStatus").value(operationStatusByHospital.get(hospital1.getId()).name()))
                .andDo(print());
        then(hospitalService).should(times(1)).getHospitalListBookmark(anyString(), any());
        then(hospitalOperationService).should(times(1)).getHospitalOperationStatus(any());
    }
    
    @DisplayName("나의 병원 리뷰 목록 조회 컨트롤러 테스트")
    @Test
    void get_my_hospital_review_list_test() throws Exception {
        //given
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put("page", List.of(String.valueOf(0)));
        queryParams.put("size", List.of(String.valueOf(10)));
        queryParams.put("sort", List.of("createdAt", "desc"));
        queryParams.put("filter", List.of(HospitalReviewFilter.ALL.name()));

        MyHospitalReviewListData.MyHospitalReviewData myHospitalReviewData1 = MyHospitalReviewListData.MyHospitalReviewData.builder()
                .hospitalReviewId("1")
                .hospitalId("1")
                .hospitalName("병원1")
                .reviewType(HospitalReviewType.RECEIPT_REVIEW)
                .createdAt(LocalDateTime.now())
                .content("리뷰1")
                .imageList(List.of("images/review1.png"))
                .build();
        MyHospitalReviewListData.MyHospitalReviewData myHospitalReviewData2 = MyHospitalReviewListData.MyHospitalReviewData.builder()
                .hospitalReviewId("2")
                .hospitalId("2")
                .hospitalName("병원2")
                .reviewType(HospitalReviewType.EVALUATION_REVIEW)
                .createdAt(LocalDateTime.now())
                .content("리뷰2")
                .imageList(List.of("images/review2.png"))
                .build();
        PagingResponse paging = PagingResponse.builder()
                .page(0)
                .size(10)
                .totalPages(1)
                .numberOfElements(2)
                .totalElements(2L)
                .isFirst(true)
                .isLast(true)
                .isEmpty(false)
                .build();
        MyHospitalReviewListData myHospitalReviewListData = MyHospitalReviewListData.builder()
                .hospitalReviewList(List.of(myHospitalReviewData1, myHospitalReviewData2))
                .paging(paging)
                .build();
        given(hospitalService.getMyHospitalReviewList(anyString(), any(), any())).willReturn(myHospitalReviewListData);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/my-reviews")
                        .queryParams(queryParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalReviewList[0].hospitalReviewId").value(myHospitalReviewData1.getHospitalReviewId()))
                .andExpect(jsonPath("$.hospitalReviewList[0].hospitalId").value(myHospitalReviewData1.getHospitalId()))
                .andExpect(jsonPath("$.hospitalReviewList[0].hospitalName").value(myHospitalReviewData1.getHospitalName()))
                .andExpect(jsonPath("$.hospitalReviewList[0].reviewType").value(myHospitalReviewData1.getReviewType().name()))
                .andExpect(jsonPath("$.hospitalReviewList[0].createdAt").value(TimeUtil.toEpochSecond(myHospitalReviewData1.getCreatedAt())))
                .andExpect(jsonPath("$.hospitalReviewList[0].content").value(myHospitalReviewData1.getContent()))
                .andExpect(jsonPath("$.hospitalReviewList[0].imageList").isArray())
                .andDo(print());
        then(hospitalService).should(times(1)).getMyHospitalReviewList(anyString(), any(), any());
    }

    @DisplayName("의사 목록 간단 조회 컨트롤러 테스트")
    @Test
    void get_doctor_brief_list_test() throws Exception {
        //given
        String hospitalId = "1";
        Doctor doctor1 = HospitalFixtures.toDoctor().id("1").name("의사1").build();
        Doctor doctor2 = HospitalFixtures.toDoctor().id("2").name("의사2").build();
        DoctorBriefListData doctorBriefListData = new DoctorBriefListData(List.of(doctor1, doctor2));
        given(hospitalService.getDoctorBriefList(hospitalId)).willReturn(doctorBriefListData);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/{hospitalId}/doctors/brief", hospitalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorList[0].doctorId").value(doctor1.getId()))
                .andExpect(jsonPath("$.doctorList[0].name").value(doctor1.getName()))
                .andExpect(jsonPath("$.doctorList[0].image").value(doctor1.getImage()))
                .andDo(print());
        then(hospitalService).should(times(1)).getDoctorBriefList(hospitalId);
    }
    
    @DisplayName("병원 상세 조회 컨트롤러 테스트")
    @Test
    void get_hospital_details_test() throws Exception {
        //given
        String hospitalId = "1";
        String placeId = "1";
        Hospital hospital = HospitalFixtures.toHospital().id(hospitalId).placeId(placeId).build();
        Doctor doctor1 = HospitalFixtures.toDoctor().id("1").name("의사1").build();
        Doctor doctor2 = HospitalFixtures.toDoctor().id("2").name("의사2").build();
        HospitalDetailsData hospitalDetailsData = HospitalDetailsData.builder()
                .hospital(hospital)
                .isBookmarked(true)
                .isEvaluationReviewed(true)
                .doctorList(List.of(doctor1, doctor2))
                .build();
        HospitalOperationDetailsData hospitalOperationDetails = HospitalOperationDetailsData.builder()
                .operationStatus(HospitalOperationStatus.OPEN)
                .operationStartHour(9)
                .operationStartMin(0)
                .operationEndHour(18)
                .operationEndMin(0)
                .build();
        String phoneRegex = "^(010|02|031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064)(\\d{3,4})(\\d{4})$";
        String phoneFormat = "$1-$2-$3";
        given(hospitalService.getHospitalDetails(anyString(), eq(hospitalId))).willReturn(hospitalDetailsData);
        given(hospitalOperationService.getHospitalOperationDetails(placeId)).willReturn(hospitalOperationDetails);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/{hospitalId}", hospitalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalId").value(hospital.getId()))
                .andExpect(jsonPath("$.name").value(hospital.getName()))
                .andExpect(jsonPath("$.address").value(hospital.getAddress()))
                .andExpect(jsonPath("$.phone").value(hospital.getPhone().replaceFirst(phoneRegex, phoneFormat)))
                .andExpect(jsonPath("$.latitude").value(hospital.getLocation().getY()))
                .andExpect(jsonPath("$.longitude").value(hospital.getLocation().getX()))
                .andExpect(jsonPath("$.totalReceiptReviewCount").value(hospital.getTotalReceiptReviewCount()))
                .andExpect(jsonPath("$.totalEvaluationReviewCount").value(hospital.getTotalEvaluationReviewCount()))
                .andExpect(jsonPath("$.isBookmarked").value(hospitalDetailsData.getIsBookmarked()))
                .andExpect(jsonPath("$.isEvaluationReviewed").value(hospitalDetailsData.getIsEvaluationReviewed()))
                .andExpect(jsonPath("$.operationStatus").value(hospitalOperationDetails.getOperationStatus().name()))
                .andExpect(jsonPath("$.operationStartHour").value(hospitalOperationDetails.getOperationStartHour()))
                .andExpect(jsonPath("$.operationStartMin").value(hospitalOperationDetails.getOperationStartMin()))
                .andExpect(jsonPath("$.operationEndHour").value(hospitalOperationDetails.getOperationEndHour()))
                .andExpect(jsonPath("$.operationEndMin").value(hospitalOperationDetails.getOperationEndMin()))

                .andExpect(jsonPath("$.doctorList[0].doctorId").value(doctor1.getId()))
                .andExpect(jsonPath("$.doctorList[0].name").value(doctor1.getName()))
                .andExpect(jsonPath("$.doctorList[0].image").value(doctor1.getImage()))
                .andExpect(jsonPath("$.doctorList[0].totalReceiptReviewCount").value(doctor1.getTotalReceiptReviewCount()))
                .andExpect(jsonPath("$.doctorList[0].profile").value(doctor1.getProfile()))
                .andDo(print());
        then(hospitalService).should(times(1)).getHospitalDetails(anyString(), eq(hospitalId));
        then(hospitalOperationService).should(times(1)).getHospitalOperationDetails(eq(placeId));
    }
    
    @DisplayName("포에이리본 병원 평가 질문 조회 컨트롤러 테스트")
    @Test
    void get_evaluation_question_list_test() throws Exception {
        //given
        HospitalEvaluationQuestion hospitalEvaluationQuestion1 = toHospitalEvaluationQuestion().id(1L).seq(1).question("평가 질문1").build();
        HospitalEvaluationQuestion hospitalEvaluationQuestion2 = toHospitalEvaluationQuestion().id(2L).seq(2).question("평가 질문2").build();
        HospitalEvaluationQuestionListData hospitalEvaluationQuestionListData = new HospitalEvaluationQuestionListData(List.of(hospitalEvaluationQuestion1, hospitalEvaluationQuestion2));
        given(hospitalService.getEvaluationQuestionList()).willReturn(hospitalEvaluationQuestionListData);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/evaluation-questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalEvaluationQuestionList[0].hospitalEvaluationQuestionId").value(hospitalEvaluationQuestion1.getId()))
                .andExpect(jsonPath("$.hospitalEvaluationQuestionList[0].seq").value(hospitalEvaluationQuestion1.getSeq()))
                .andExpect(jsonPath("$.hospitalEvaluationQuestionList[0].question").value(hospitalEvaluationQuestion1.getQuestion()))
                .andDo(print());
        then(hospitalService).should(times(1)).getEvaluationQuestionList();
    }
    
    @DisplayName("포에이리본 병원 평가 답변 조회 컨트롤러 테스트")
    @Test
    void get_evaluation_review_test() throws Exception {
        //given
        String hospitalEvaluationReviewId = "1";
        HospitalEvaluationAnswer hospitalEvaluationAnswer1 = HospitalFixtures.toHospitalEvaluationAnswer()
                .id(1L)
                .hospitalEvaluationReview(toHospitalEvaluationReview().build())
                .hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(1L).seq(1).question("평가 질문1").build())
                .answer(true)
                .build();
        HospitalEvaluationAnswer hospitalEvaluationAnswer2 = HospitalFixtures.toHospitalEvaluationAnswer()
                .id(2L)
                .hospitalEvaluationReview(toHospitalEvaluationReview().build())
                .hospitalEvaluationQuestion(toHospitalEvaluationQuestion().id(2L).seq(2).question("평가 질문2").build())
                .answer(false)
                .build();
        HospitalEvaluationReviewData hospitalEvaluationReviewData = new HospitalEvaluationReviewData(List.of(hospitalEvaluationAnswer1, hospitalEvaluationAnswer2));
        given(hospitalService.getEvaluationReview(anyString(), eq(hospitalEvaluationReviewId))).willReturn(hospitalEvaluationReviewData);

        //when, then
        mockMvc.perform(get("/api/v1/hospitals/evaluation-reviews/{hospitalEvaluationReviewId}", hospitalEvaluationReviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalEvaluationAnswerList[0].hospitalEvaluationQuestionId").value(hospitalEvaluationAnswer1.getHospitalEvaluationQuestion().getId()))
                .andExpect(jsonPath("$.hospitalEvaluationAnswerList[0].seq").value(hospitalEvaluationAnswer1.getHospitalEvaluationQuestion().getSeq()))
                .andExpect(jsonPath("$.hospitalEvaluationAnswerList[0].question").value(hospitalEvaluationAnswer1.getHospitalEvaluationQuestion().getQuestion()))
                .andExpect(jsonPath("$.hospitalEvaluationAnswerList[0].answer").value(hospitalEvaluationAnswer1.getAnswer()))
                .andDo(print());
        then(hospitalService).should(times(1)).getEvaluationReview(anyString(), eq(hospitalEvaluationReviewId));
    }
    
    @DisplayName("포에이리본 병원 평가하기 컨트롤러 테스트")
    @Test
    void create_evaluation_review_test() throws Exception {
        //given
        String hospitalId = "1";
        HospitalEvaluationReviewCreateRequest request = HospitalEvaluationReviewCreateRequest.builder()
                .hospitalEvaluationAnswerList(List.of(
                        HospitalEvaluationReviewCreateRequest.HospitalEvaluationAnswerCreateRequest.builder()
                                .hospitalEvaluationQuestionId(1L)
                                .answer(true)
                                .build(),
                        HospitalEvaluationReviewCreateRequest.HospitalEvaluationAnswerCreateRequest.builder()
                                .hospitalEvaluationQuestionId(2L)
                                .answer(false)
                                .build()))
                .build();
        willDoNothing().given(hospitalService).createEvaluationReview(anyString(), eq(hospitalId), any());

        //when, then
        mockMvc.perform(post("/api/v1/hospitals/{hospitalId}/evaluation-reviews", hospitalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
        then(hospitalService).should(times(1)).createEvaluationReview(anyString(), eq(hospitalId), any());
    }
    
    @DisplayName("포에이리본 병원 평가하기 수정 컨트롤러 테스트")
    @Test
    void update_evaluation_review_test() throws Exception {
        //given
        String hospitalEvaluationReviewId = "1";
        HospitalEvaluationReviewUpdateRequest request = new HospitalEvaluationReviewUpdateRequest(List.of(
                HospitalEvaluationReviewUpdateRequest.HospitalEvaluationAnswerUpdateRequest.builder()
                        .hospitalEvaluationQuestionId(1L)
                        .answer(true)
                        .build(),
                HospitalEvaluationReviewUpdateRequest.HospitalEvaluationAnswerUpdateRequest.builder()
                        .hospitalEvaluationQuestionId(2L)
                        .answer(false)
                        .build()));
        willDoNothing().given(hospitalService).updateEvaluationReview(anyString(), eq(hospitalEvaluationReviewId), any());

        //when, then
        mockMvc.perform(put("/api/v1/hospitals/evaluation-reviews/{hospitalEvaluationReviewId}", hospitalEvaluationReviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
        then(hospitalService).should(times(1)).updateEvaluationReview(anyString(), eq(hospitalEvaluationReviewId), any());
    }
    
    @DisplayName("포에이리본 병원 평가하기 삭제 컨트롤러 테스트")
    @Test
    void delete_evaluation_review_test() throws Exception {
        //given
        String hospitalEvaluationReviewId = "1";
        willDoNothing().given(hospitalService).deleteEvaluationReview(anyString(), eq(hospitalEvaluationReviewId));
        
        //when, then
        mockMvc.perform(delete("/api/v1/hospitals/evaluation-reviews/{hospitalEvaluationReviewId}", hospitalEvaluationReviewId))
                .andExpect(status().isOk())
                .andDo(print());
        then(hospitalService).should(times(1)).deleteEvaluationReview(anyString(), eq(hospitalEvaluationReviewId));
    }
    
    @DisplayName("병원 북마크 컨트롤러 테스트")
    @Test
    void bookmark_hospital_test() throws Exception {
        //given
        String hospitalId = "1";
        Boolean bookmark = true;
        willDoNothing().given(hospitalService).saveHospitalBookmark(anyString(), eq(hospitalId), eq(bookmark));
        
        //when, then
        mockMvc.perform(post("/api/v1/hospitals/{hospitalId}/bookmark", hospitalId)
                        .param("bookmark", String.valueOf(bookmark)))
                .andExpect(status().isOk())
                .andDo(print());
        then(hospitalService).should(times(1)).saveHospitalBookmark(anyString(), eq(hospitalId), eq(bookmark));
    }
    
    @DisplayName("병원 영수증 리뷰 작성 컨트롤러 테스트")
    @Test
    void create_hospital_receipt_review_test() throws Exception {
        //given
        String hospitalId = "1";
        HospitalReceiptReviewCreateRequest request = HospitalReceiptReviewCreateRequest.builder()
                .content("영수증 리뷰 내용")
                .imageList(List.of("/images/review1.png", "/images/review2.png"))
                .medicalExpense(15000L)
                .build();
        willDoNothing().given(hospitalService).createReceiptReview(anyString(), eq(hospitalId), any());

        //when, then
        mockMvc.perform(post("/api/v1/hospitals/{hospitalId}/receipt-reviews", hospitalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
        then(hospitalService).should(times(1)).createReceiptReview(anyString(), eq(hospitalId), any());
    }
    
    @DisplayName("의사 영수증 리뷰 작성 컨트롤러 테스트")
    @Test
    void create_doctor_receipt_review_test() throws Exception {
        //given
        String hospitalId = "1";
        String doctorId = "1";
        HospitalReceiptReviewCreateRequest request = HospitalReceiptReviewCreateRequest.builder()
                .content("영수증 리뷰 내용")
                .imageList(List.of("/images/review1.png", "/images/review2.png"))
                .medicalExpense(15000L)
                .build();
        willDoNothing().given(hospitalService).createReceiptReview(anyString(), eq(hospitalId), eq(doctorId), any());
        
        //when, then
        mockMvc.perform(post("/api/v1/hospitals/{hospitalId}/doctors/{doctorId}/receipt-reviews", hospitalId, doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
        then(hospitalService).should(times(1)).createReceiptReview(anyString(), eq(hospitalId), eq(doctorId), any());
    }
    
    @DisplayName("영수증 리뷰 도음돼요 컨트롤러 테스트")
    @Test
    void mark_receipt_review_as_helpful_test() throws Exception {
        //given
        String hospitalReceiptReviewId = "1";
        Boolean help = true;
        willDoNothing().given(hospitalService).markReceiptReviewAsHelpful(anyString(), eq(hospitalReceiptReviewId), eq(help));
        
        //when, then
        mockMvc.perform(post("/api/v1/hospitals/receipt-reviews/{hospitalReceiptReviewId}/help", hospitalReceiptReviewId)
                        .param("help", String.valueOf(help)))
                .andExpect(status().isOk())
                .andDo(print());
        then(hospitalService).should(times(1)).markReceiptReviewAsHelpful(anyString(), eq(hospitalReceiptReviewId), eq(help));
    }
    
    @DisplayName("영수증 리뷰 수정 컨트롤러 테스트")
    @Test
    void update_receipt_review_test() throws Exception {
        //given
        String hospitalReceiptReviewId = "1";
        HospitalReceiptReviewUpdateRequest request = HospitalReceiptReviewUpdateRequest.builder()
                .content("영수증 리뷰 수정 내용")
                .imageList(List.of("/images/review1.png", "/images/review2.png"))
                .medicalExpense(15000L)
                .build();
        willDoNothing().given(hospitalService).updateReceiptReview(anyString(), eq(hospitalReceiptReviewId), any());

        //when, then
        mockMvc.perform(put("/api/v1/hospitals/receipt-reviews/{hospitalReceiptReviewId}", hospitalReceiptReviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
        then(hospitalService).should(times(1)).updateReceiptReview(anyString(), eq(hospitalReceiptReviewId), any());
    }
    
    @DisplayName("영수증 리뷰 삭제 컨트롤러 테스트")
    @Test
    void delete_receipt_review_test() throws Exception {
        //given
        String hospitalReceiptReviewId = "1";
        willDoNothing().given(hospitalService).deleteReceiptReview(anyString(), eq(hospitalReceiptReviewId));
        
        //when, then
        mockMvc.perform(delete("/api/v1/hospitals/receipt-reviews/{hospitalReceiptReviewId}", hospitalReceiptReviewId))
                .andExpect(status().isOk())
                .andDo(print());
        then(hospitalService).should(times(1)).deleteReceiptReview(anyString(), eq(hospitalReceiptReviewId));
    }
}
