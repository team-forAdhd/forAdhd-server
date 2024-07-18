package com.project.foradhd.domain.board.web.dto.response;

import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostRankingResponseDto {
    private Long id;
    private String title;
    private CategoryName category;
    private long viewCount;
    private long likeCount;
    private LocalDateTime createdAt;
    private List<String> images;
}

