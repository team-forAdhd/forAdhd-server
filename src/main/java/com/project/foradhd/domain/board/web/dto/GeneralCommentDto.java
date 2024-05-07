package com.project.foradhd.domain.board.web.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralCommentDto {
    private String commentId;
    private String writerId;
    private String writerName;
    private String postId;
    private int postType;
    private String parentCommentId;
    private String content;
    private boolean anonymous;
    private long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
