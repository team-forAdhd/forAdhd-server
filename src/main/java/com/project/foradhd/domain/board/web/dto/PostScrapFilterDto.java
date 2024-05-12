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
    private String userId;
    private LocalDateTime createdAt;

    public PostScrapFilterDto(Long scrapId, Long postId, String userId, LocalDateTime createdAt) {
        this.scrapId = scrapId;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
