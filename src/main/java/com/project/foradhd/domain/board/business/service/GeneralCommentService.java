package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import java.util.List;

public interface GeneralCommentService {
    GeneralCommentDto getComment(String Id);
    GeneralCommentDto createComment(GeneralCommentDto commentDTO);
    void deleteComment(String Id);

    GeneralCommentDto updateComment(GeneralCommentDto commentDTO);
    List<GeneralCommentDto> listCommentsByPostId(String postId);
    List<GeneralCommentDto> listComments();
}

