package com.project.foradhd.domain.board.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String userId;
    private String category;
    private String title;
    private String content;
    private String nickname;
    private String profileImage;
    private boolean anonymous;
    private List<String> images;
    private long likeCount;
    private long commentCount;
    private long scrapCount;
    private long viewCount;
}
