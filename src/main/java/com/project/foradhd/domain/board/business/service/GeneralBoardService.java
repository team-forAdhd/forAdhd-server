package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import java.util.List;

public interface GeneralBoardService {

    GeneralPostDto getPost(String postId);
    GeneralPostDto createPost(GeneralPostDto postDTO);

    void deletePost(String postId);

    GeneralPostDto updatePost(GeneralPostDto postDTO);
    List<GeneralPostDto> listPosts();
}
