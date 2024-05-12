package com.project.foradhd.domain.board.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GeneralPostDto {
    private Long postId;
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
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    GeneralPostDto(Long postId, Long writerId, String writerName, Long categoryId, String categoryName,
                   String title, String content, boolean anonymous, String images, long likeCount,
                   long commentCount, long scrapCount, long viewCount, String tags,
                   LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.postId = postId;
        this.writerId = writerId;
        this.writerName = writerName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.title = title;
        this.content = content;
        this.anonymous = anonymous;
        this.images = images;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.scrapCount = scrapCount;
        this.viewCount = viewCount;
        this.tags = tags;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
