package com.project.foradhd.domain.board.business.service;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeneralPostService {
    // 특정 게시글 조회
    GeneralPostDto getPost(Long postId);

    // 새 게시글 생성
    GeneralPostDto createPost(GeneralPostDto postDTO);

    // 기존 게시글 업데이트
    GeneralPostDto updatePost(GeneralPostDto postDTO);

    // 특정 카테고리의 게시글 목록 조회
    Page<GeneralPostDto> listPosts(String category, Pageable pageable);

    // 게시글 삭제
    void deletePost(Long postId);

    // 모든 게시글 목록 조회
    Page<GeneralPostDto> getAllPosts(Pageable pageable);

    // 사용자가 스크랩한 게시글 목록 조회
    Page<GeneralPostDto> getMyScraps(String userId, Pageable pageable);

    // 특정 사용자가 작성한 게시글 목록 조회, 정렬 옵션 추가
    Page<GeneralPostDto> getUserPosts(String userId, Pageable pageable, SortOption sortOption);
}
