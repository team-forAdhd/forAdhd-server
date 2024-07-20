package com.project.foradhd.domain.board.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostScrapFilterResponseDto {
    private Long postId;
    private String userId;
    private String postTitle;
    private String category;
    private long viewCount;
    private long likeCount;
    private long commentCount;
    private String imageUrl;
}
