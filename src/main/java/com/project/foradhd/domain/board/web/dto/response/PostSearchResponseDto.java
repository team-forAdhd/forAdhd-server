package com.project.foradhd.domain.board.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchResponseDto {
    private List<PostSearchListResponseDto> data;
    private PagingResponse paging;
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostSearchListResponseDto {
        private Long id;
        private String title;
        private long viewCount;
        private long likeCount;
        private long commentCount;
        @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
        private LocalDateTime createdAt;
        private List<String> images;
    }
}
