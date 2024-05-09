package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GeneralCommentService {
    GeneralCommentDto getComment(String commentId);
    GeneralCommentDto createComment(GeneralCommentDto commentDTO);
    void deleteComment(String commentId);
    GeneralCommentDto updateComment(GeneralCommentDto commentDTO);
    void toggleCommentLike(String userId, String commentId);
    Page<GeneralCommentDto> getMyComments(String writerId, Pageable pageable);
    Page<GeneralCommentDto> getCommentsByPost(String postId, Pageable pageable, SortOption sortOption);
}

