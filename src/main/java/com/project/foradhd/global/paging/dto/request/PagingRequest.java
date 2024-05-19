package com.project.foradhd.global.paging.dto.request;

import com.project.foradhd.global.paging.enums.Sort.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagingRequest {

    private int page;
    private int size;
    private List<SortRequest> sort;

    @Getter
    @AllArgsConstructor
    public static class SortRequest {

        private String property;
        private Direction direction;
    }

    public Pageable to() {
        return PageRequest.of(page, size);
    }
}
