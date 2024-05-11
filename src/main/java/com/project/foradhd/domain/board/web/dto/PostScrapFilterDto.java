package com.project.foradhd.domain.board.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostScrapFilterDto {
    private Long scrapId;
    private Long postId;
    private Long userId;
    private LocalDateTime createdAt;

    // Constructors for @Builder
    public PostScrapFilterDto(Long scrapId, Long postId, Long userId, LocalDateTime createdAt) {
        this.scrapId = scrapId;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
