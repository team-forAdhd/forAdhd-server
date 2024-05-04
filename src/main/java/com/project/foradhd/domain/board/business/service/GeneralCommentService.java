package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import java.util.List;

public interface GeneralCommentService {
    GeneralCommentDto getComment(String commentId);
    GeneralCommentDto createComment(GeneralCommentDto commentDto);
    void deleteComment(String commentId);
    GeneralCommentDto updateComment(GeneralCommentDto commentDto);
    List<GeneralCommentDto> listComments();
}
