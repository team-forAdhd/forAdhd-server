package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralPostRepository extends JpaRepository<GeneralPost, Long> {  // ID 타입을 Long으로 변경
    Page<GeneralPost> findByWriterId(Long writerId, Pageable pageable);  // writerId 타입을 Long으로 변경
    Page<GeneralPost> findByCategoryId(Long categoryId, Pageable pageable);  // categoryId 타입을 Long으로 변경
    Page<GeneralPost> findByUserId(Long userId, Pageable pageable);  // userId 타입을 Long으로 변경

    // 게시글 좋아요 기능
    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
    void incrementLikeCount(Long postId);  // postId 타입을 Long으로 변경

    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId")
    void decrementLikeCount(Long postId);  // postId 타입을 Long으로 변경
}
