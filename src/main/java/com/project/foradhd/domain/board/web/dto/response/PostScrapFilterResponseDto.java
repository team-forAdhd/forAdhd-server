package com.project.foradhd.domain.board.web.dto.response;

import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostScrapFilterResponseDto {

    private List<PostScrapFilterListResponseDto> postScrapList;
    private PagingResponse paging;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostScrapFilterListResponseDto {
        private Long postId;
        private String userId;
        private String postTitle;
        private String category;
        private long viewCount;
        private long likeCount;
        private long commentCount;
        private String imageUrl;
    }
}
