package com.project.foradhd.domain.board.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDto {
    private Long Id;
    private Long writerId;
    private String writerName;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String content;
    private boolean anonymous;
    private String images;
    private long likeCount;
    private long commentCount;
    private long scrapCount;
    private long viewCount;
    private String tags;
}
