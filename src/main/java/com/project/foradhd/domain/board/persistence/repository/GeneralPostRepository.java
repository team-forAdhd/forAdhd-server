package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralPostRepository extends JpaRepository<GeneralPost, Long> {
    Page<GeneralPost> findByWriterId(Long writerId, Pageable pageable);
    Page<GeneralPost> findByCategoryId(Long categoryId, Pageable pageable);
    Page<GeneralPost> findByUserId(Long userId, Pageable pageable);

    // 게시글 좋아요 기능
    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
    void incrementLikeCount(Long postId);

    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId")
    void decrementLikeCount(Long postId);
}
