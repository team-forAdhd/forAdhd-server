package com.project.foradhd.domain.board.web.dto.response;

import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private boolean anonymous;
    private List<String> images;
    private long likeCount;
    private long commentCount;
    private long scrapCount;
    private long viewCount;
    private CategoryName category;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
