package com.project.foradhd.domain.board.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PostScrapFilterDto {
    private Long scrapId;
    private Long postId;
    private String userId;
}
