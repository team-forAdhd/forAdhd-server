package com.project.foradhd.domain.board.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDto {
    private Long commentId;
    private Long postId;
    private Long writerId;
    private String content;
    private boolean anonymous;
    private long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    // Constructors for @Builder
    CommentDto(Long commentId, Long postId, Long writerId, String content,
               boolean anonymous, long likeCount, LocalDateTime createdAt,
               LocalDateTime lastModifiedAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.writerId = writerId;
        this.content = content;
        this.anonymous = anonymous;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
