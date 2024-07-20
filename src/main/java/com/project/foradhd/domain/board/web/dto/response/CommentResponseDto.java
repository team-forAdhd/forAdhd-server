package com.project.foradhd.domain.board.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final String userId;
    private final Long postId;
    private final boolean anonymous;
    private final long likeCount;

    @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
    private final LocalDateTime createdAt;

    private final CommentResponseDto parentComment;
    private final List<CommentResponseDto> children;
    private final String nickname;
    private final String profileImage;
}
