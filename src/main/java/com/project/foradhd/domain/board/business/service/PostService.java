package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.response.PostRankingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostService {
    // 특정 게시글 조회
    Post getPost(Long Id);

    // 새 게시글 생성
    Post createPost(Post post);

    // 기존 게시글 업데이트
    @Transactional
    Post updatePost(Post post);

    // 특정 카테고리의 게시글 목록 조회
    Page<Post> listByCategory(CategoryName category, Pageable pageable);

    // 게시글 삭제
    void deletePost(Long Id);

    // 모든 게시글 목록 조회
    Page<Post> getAllPosts(Pageable pageable);

    // 특정 사용자가 작성한 게시글 목록 조회, 정렬 옵션 추가
    Page<Post> getUserPosts(String userId, Pageable pageable, SortOption sortOption);

    // 글 조회수
    Post getAndIncrementViewCount(Long postId);

    List<PostRankingResponseDto> getTopPosts(Pageable pageable);
    List<PostRankingResponseDto> getTopPostsByCategory(CategoryName category, Pageable pageable);
}
