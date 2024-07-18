package com.project.foradhd.domain.board.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private Long writerId;
    private String content;
    private boolean anonymous;
    private long likeCount;
}
