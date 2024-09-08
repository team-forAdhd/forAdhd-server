package com.project.foradhd.domain.board.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostRankingResponseDto {
    private Long id;
    private String userId;
    private String title;
    private CategoryName category;
    private long viewCount;
    private long likeCount;
    @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
    private LocalDateTime createdAt;
    private List<String> images;
}