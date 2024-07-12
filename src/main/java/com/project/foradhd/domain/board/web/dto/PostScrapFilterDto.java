package com.project.foradhd.domain.board.web.dto;

import lombok.*;


@Getter
@AllArgsConstructor
@Builder
public class PostScrapFilterDto {
    private Long id;
    private Long postId;
    private String userId;
}
