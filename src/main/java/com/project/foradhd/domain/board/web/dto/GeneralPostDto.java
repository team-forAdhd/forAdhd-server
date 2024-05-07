package com.project.foradhd.domain.board.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GeneralPostDto {
    private String postId;
    private String writerId;
    private String categoryId;
    private String writerName;
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
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
