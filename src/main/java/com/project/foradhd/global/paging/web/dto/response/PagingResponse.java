package com.project.foradhd.global.paging.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class PagingResponse {

    private Integer page; //현재 페이지 번호
    private Integer size; //한 페이지 사이즈
    private Integer totalPages; //총 페이지 수
    private Integer numberOfElements; //현재 페이지 내 데이터 수
    private Long totalElements; //전체 데이터 수
    private Boolean isFirst; //첫 페이지 여부
    private Boolean isLast; //마지막 페이지 여부
    private Boolean isEmpty; //현재 페이지 내 데이터 존재 여부

    public static PagingResponse from(Page<?> paging) {
        return PagingResponse.builder()
                .page(paging.getNumber())
                .size(paging.getSize())
                .totalPages(paging.getTotalPages())
                .numberOfElements(paging.getNumberOfElements())
                .totalElements(paging.getTotalElements())
                .isFirst(paging.isFirst())
                .isLast(paging.isLast())
                .isEmpty(paging.isEmpty())
                .build();
    }

    public static PagingResponse from(int page, int size, int numberOfElements, long totalElements) {
        int totalPages = (int) (totalElements + size - 1) / size;
        return PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .numberOfElements(numberOfElements)
                .totalElements(totalElements)
                .isFirst(page == 0)
                .isLast(page == totalPages - 1)
                .isEmpty(numberOfElements == 0)
                .build();
    }
}
