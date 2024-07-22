package com.project.foradhd.domain.board.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private List<PostListResponseDto> postList;
    private PagingResponse paging;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostListResponseDto {
        private Long id;
        private String userId;
        private String title;
        private String content;
        private boolean anonymous;
        private List<String> images;
        private long likeCount;
        private long commentCount;
        private long scrapCount;
        private long viewCount;
        private CategoryName category;
        private List<CommentResponseDto.CommentListResponseDto> comments; // 수정된 부분
        private String nickname;
        private String profileImage;

        @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
        private LocalDateTime createdAt;
        @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
        private LocalDateTime lastModifiedAt;
    }
}
