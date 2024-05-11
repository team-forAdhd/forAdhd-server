package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto getComment(Long commentId);
    CommentDto createComment(CommentDto commentDto);
    void deleteComment(Long commentId);
    CommentDto updateComment(CommentDto commentDto);
    void toggleCommentLike(Long userId, Long commentId);
    Page<CommentDto> getMyComments(Long writerId, Pageable pageable);
    Page<CommentDto> getCommentsByPost(Long postId, Pageable pageable, SortOption sortOption);
}
