package com.project.foradhd.domain.board.web.dto.response;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Builder
@RequiredArgsConstructor
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final String userId;
    private final Long postId;
    private final boolean anonymous;
    private final long likeCount;
    private final LocalDateTime createdAt;
    private final Comment parentComment;
    private final List<CommentResponseDto> children;
    private final String nickname;
    private final String profileImage;
}
