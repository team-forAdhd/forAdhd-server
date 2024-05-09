package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeneralBoardService {
    GeneralPostDto getPost(String postId);
    GeneralPostDto createPost(GeneralPostDto postDTO);
    GeneralPostDto updatePost(GeneralPostDto postDTO);
    Page<GeneralPostDto> listPosts(String category, Pageable pageable);

    void deletePost(String postId);

    Page<GeneralPostDto> getAllPosts(Pageable pageable);
    Page<GeneralPostDto> getMyPosts(String writerId, Pageable pageable);
    Page<GeneralPostDto> getMyScraps(String userId, Pageable pageable);
    Page<GeneralPostDto> getPostsByCategory(String categoryId, Pageable pageable);
    // 내가 작성한 글 정렬 옵션 추가
    Page<GeneralPostDto> getUserPosts(String userId, Pageable pageable, SortOption sortOption);
}
