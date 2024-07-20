package com.project.foradhd.domain.board.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommentRequestDto {
    private String userId;
    private Long postId;
    private Long parentCommentId;
    private String content;
    private boolean anonymous;
}
