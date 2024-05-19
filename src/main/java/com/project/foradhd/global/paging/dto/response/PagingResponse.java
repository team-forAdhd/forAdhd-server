package com.project.foradhd.global.paging.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PagingResponse<T> {

    private List<T> data;
    private int page; //현재 페이지 번호
    private int size; //한 페이지 사이즈
    private int totalPages; //총 페이지 수
    private int numberOfElements; //현재 페이지 내 데이터 수
    private long totalElements; //전체 데이터 수
    private boolean isFirst; //첫 페이지 여부
    private boolean isLast; //마지막 페이지 여부
    private boolean isEmpty; //현재 페이지 내 데이터 존재 여부

    public static <T> PagingResponse<T> from(Page<T> page) {
        return PagingResponse.<T>builder()
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .isEmpty(page.isEmpty())
                .build();
    }
}
